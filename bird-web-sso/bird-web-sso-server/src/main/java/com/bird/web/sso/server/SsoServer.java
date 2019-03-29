package com.bird.web.sso.server;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.server.client.IClientStore;
import com.bird.web.sso.server.event.SsoServerLoginEvent;
import com.bird.web.sso.server.event.SsoServerLogoutEvent;
import com.bird.web.sso.server.event.SsoServerRefreshTicketEvent;
import com.bird.web.sso.server.ticket.ITicketProtector;
import com.bird.web.sso.server.ticket.ITicketSessionStore;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.utils.CookieHelper;
import com.bird.web.sso.utils.HttpClient;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liuxx
 * @date 2019/3/1
 */
@Slf4j
public class SsoServer {

    private final static String REMOVE_CLIENT_TICKET_URL = "/sso/client/ticket/removeCache?token=";

    private SsoServerProperties serverProperties;

    private IClientStore clientStore;
    private ITicketSessionStore sessionStore;
    private ITicketProtector protector;

    @Autowired(required = false)
    private EventBus eventBus;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public SsoServer(SsoServerProperties serverProperties, IClientStore clientStore, ITicketSessionStore sessionStore) {
        this.serverProperties = serverProperties;
        this.clientStore = clientStore;
        this.sessionStore = sessionStore;
    }

    public SsoServer(SsoServerProperties serverProperties, IClientStore clientStore, ITicketProtector protector) {
        this.serverProperties = serverProperties;
        this.clientStore = clientStore;
        this.protector = protector;
    }

    /**
     * 登录，将token写入cookie
     *
     * @param ticketInfo 票据信息
     * @return token
     */
    public String login(HttpServletResponse response, TicketInfo ticketInfo) {
        if (ticketInfo.getExpireTime() == null) {
            Date creationTime = ticketInfo.getCreationTime();
            long expire = creationTime.getTime() + serverProperties.getExpire() * 60 * 1000L;
            ticketInfo.setExpireTime(new Date(expire));
        }
        String token = serverProperties.getUseSessionStore()
                ? sessionStore.storeTicket(ticketInfo)
                : protector.protect(ticketInfo);

        //用户中心写入Cookie
        CookieHelper.setCookie(response, serverProperties.getCookieName(), StringUtils.strip(token), serverProperties.getExpire() * 60);
        //触发事件
        this.postEvent(new SsoServerLoginEvent(token, ticketInfo));
        return token;
    }

    /**
     * 注销
     * 1、清除SessionStore；2、清除Cookie
     *
     * @return
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request);

        if (!StringUtils.isEmpty(token)) {
            TicketInfo ticketInfo = serverProperties.getUseSessionStore()
                    ? sessionStore.getTicket(token)
                    : protector.unProtect(token);

            //清除SessionStore
            if (sessionStore != null) {
                sessionStore.removeTicket(token);
            }
            //清除Cookie
            CookieHelper.removeCookie(request, response, serverProperties.getCookieName());
            //通知客户端，移除缓存
            executorService.execute(() -> this.removeClientCache(token));
            //触发事件
            this.postEvent(new SsoServerLogoutEvent(token, ticketInfo));
        }
    }

    /**
     * 根据token获取票据信息
     *
     * @param token token
     * @return 票据
     */
    public TicketInfo getTicket(String clientHost, String token) {
        if (StringUtils.isBlank(clientHost) || StringUtils.isBlank(token)) return null;
        clientStore.store(token, clientHost);

        TicketInfo ticketInfo;
        if (serverProperties.getUseSessionStore()) {
            ticketInfo = sessionStore.getTicket(token);
            if (ticketInfo != null && serverProperties.getAutoRefresh()) {
                //如果超过一半的有效期，则刷新
                Date now = new Date();
                Date issuedTime = ticketInfo.getLastRefreshTime();
                Date expireTime = ticketInfo.getExpireTime();

                long t1 = now.getTime() - issuedTime.getTime();
                long t2 = expireTime.getTime() - now.getTime();
                if (t1 > t2) {
                    TicketInfo origin = ticketInfo.clone();
                    ticketInfo = sessionStore.refreshTicket(token, ticketInfo, t1 + t2);
                    //触发事件
                    this.postEvent(new SsoServerRefreshTicketEvent(token, origin, true, ticketInfo));
                }
            }
        } else {
            ticketInfo = protector.unProtect(token);
        }
        return ticketInfo;
    }

    /**
     * 刷新票据
     *
     * @param token      token
     * @param ticketInfo ticketInfo
     */
    public void refreshTicket(String token, TicketInfo ticketInfo) {
        if (StringUtils.isBlank(token) || ticketInfo == null || !serverProperties.getUseSessionStore()) return;

        TicketInfo curTicket = sessionStore.getTicket(token);
        if (curTicket == null) {
            log.warn("试图刷新一个不存在的票据信息，token：" + token);
            return;
        }
        if (!StringUtils.equals(curTicket.getUserId(), ticketInfo.getUserId())) {
            log.warn("刷新的票据信息UserId不正确，token：" + token);
            return;
        }

        sessionStore.refreshTicket(token, ticketInfo, serverProperties.getExpire() * 60 * 1000L);
        //触发事件
        this.postEvent(new SsoServerRefreshTicketEvent(token, curTicket, false, ticketInfo));
    }

    /**
     * 从HttpServletRequest中获取token
     *
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        //先从header中获取token
        String token = request.getHeader(serverProperties.getCookieName());
        if (StringUtils.isBlank(token)) {
            token = CookieHelper.getCookieValue(request, serverProperties.getCookieName());
        }
        return token;
    }

    /**
     * 触发事件
     *
     * @param event
     */
    private void postEvent(SsoEvent event) {
        if (eventBus == null || event == null) return;
        eventBus.post(event);
    }

    /**
     * 移除客户端token对应的Ticket缓存
     */
    private void removeClientCache(String token) {
        List<String> clientHosts = clientStore.getAll(token);
        if (CollectionUtils.isEmpty(clientHosts)) return;

        for (String clientHost : clientHosts) {
            Integer retryCount = 3;
            String url = clientHost + REMOVE_CLIENT_TICKET_URL + token;
            List<String> headers = Arrays.asList("Accept-Encoding", "gzip,deflate,sdch");
            int resCode = 0;
            do {
                try {
                    HttpClient.HttpResult result = HttpClient.httpGet(url, headers, null, HttpClient.DEFAULT_CONTENT_TYPE);
                    if (HttpURLConnection.HTTP_OK != result.code) {
                        throw new IOException("Error while requesting: " + url + "'. Server returned: " + result.code);
                    }
                    resCode = result.code;

                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            } while (HttpURLConnection.HTTP_OK != resCode && retryCount-- > 0);
        }

        //移除client缓存
        clientStore.remove(token);
    }
}
