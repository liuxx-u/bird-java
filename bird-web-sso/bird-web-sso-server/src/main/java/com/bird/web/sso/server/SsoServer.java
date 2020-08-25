package com.bird.web.sso.server;

import com.alibaba.fastjson.JSON;
import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.server.client.IClientStore;
import com.bird.web.sso.server.event.SsoClientObtainTicketEvent;
import com.bird.web.sso.server.event.SsoServerLoginEvent;
import com.bird.web.sso.server.event.SsoServerLogoutEvent;
import com.bird.web.sso.server.event.SsoServerRefreshTicketEvent;
import com.bird.web.sso.server.ticket.ITicketProtector;
import com.bird.web.sso.server.ticket.ITicketSessionStore;
import com.bird.web.sso.ticket.ClientTicket;
import com.bird.web.sso.ticket.ServerTicket;
import com.bird.web.sso.utils.CookieHelper;
import com.bird.web.sso.utils.HttpClient;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.*;
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
    private long halfCacheMillis;

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
        this.halfCacheMillis = serverProperties.getExpire() * 60 * 1000 / 2L;
    }

    public SsoServer(SsoServerProperties serverProperties, IClientStore clientStore, ITicketProtector protector) {
        this.serverProperties = serverProperties;
        this.clientStore = clientStore;
        this.protector = protector;
    }

    /**
     * 登录，将token写入cookie
     *
     * @param serverTicket 票据信息
     * @return token
     */
    public String login(HttpServletResponse response, ServerTicket serverTicket) {
        if (serverTicket.getExpireTime() == null) {
            Date creationTime = serverTicket.getCreationTime();
            long expire = creationTime.getTime() + serverProperties.getExpire() * 60 * 1000L;
            serverTicket.setExpireTime(new Date(expire));
        }
        String token = serverProperties.getUseSessionStore()
                ? sessionStore.storeTicket(serverTicket)
                : protector.protect(serverTicket);

        //用户中心写入Cookie
        CookieHelper.setCookie(response, serverProperties.getCookieName(), StringUtils.strip(token), serverProperties.getExpire() * 60);
        //触发登录事件
        this.postEvent(new SsoServerLoginEvent(token, serverTicket));
        return token;
    }

    /**
     * 注销
     * 1、清除SessionStore；2、清除Cookie
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request);

        if (!StringUtils.isEmpty(token)) {
            ServerTicket serverTicket = serverProperties.getUseSessionStore()
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
            //触发注销事件
            this.postEvent(new SsoServerLogoutEvent(token, serverTicket));
        }
    }

    /**
     * 根据token获取票据信息
     *
     * @param token token
     * @return 票据
     */
    public ServerTicket getTicket(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        ServerTicket serverTicket;
        if (serverProperties.getUseSessionStore()) {
            serverTicket = sessionStore.getTicket(token);
            if (serverTicket != null && serverProperties.getAutoRefresh()) {
                //如果超过一半的有效期，则刷新
                long span = System.currentTimeMillis() - serverTicket.getLastRefreshTime().getTime();
                if(span > this.halfCacheMillis ){
                    ServerTicket origin = JSON.parseObject(JSON.toJSONString(serverTicket),ServerTicket.class);
                    serverTicket = sessionStore.refreshTicket(token, serverTicket, Duration.ofMinutes(serverProperties.getExpire()));
                    //触发票据刷新事件
                    this.postEvent(new SsoServerRefreshTicketEvent(token, origin, true, serverTicket));
                }
            }
        } else {
            serverTicket = protector.unProtect(token);
        }
        return serverTicket;
    }

    /**
     * 获取客户端票据信息
     * @param appId appId
     * @param clientHost clientHost
     * @param token token
     * @return 客户端票据
     */
    public ClientTicket getClientTicket(String appId,String clientHost, String token){
        if (StringUtils.isBlank(clientHost) || StringUtils.isBlank(token)) {
            return null;
        }
        clientStore.store(token, clientHost);

        ServerTicket serverTicket = this.getTicket(token);
        if(serverTicket == null){
            return null;
        }

        ClientTicket clientTicket = serverTicket.extractClientTicket(appId);
        //触发客户端获取票据事件
        this.postEvent(new SsoClientObtainTicketEvent(token, serverTicket, clientHost));
        return clientTicket;
    }

    /**
     * 刷新票据
     *
     * @param token      token
     * @param serverTicket serverTicket
     */
    public void refreshTicket(String token, ServerTicket serverTicket) {
        if (StringUtils.isBlank(token) || serverTicket == null || !serverProperties.getUseSessionStore()) {
            return;
        }

        ServerTicket curTicket = sessionStore.getTicket(token);
        if (curTicket == null) {
            log.warn("试图刷新一个不存在的票据信息，token：" + token);
            return;
        }

        sessionStore.refreshTicket(token, serverTicket, Duration.ofMinutes(serverProperties.getExpire()));
        //通知客户端，移除缓存
        executorService.execute(() -> this.removeClientCache(token));
        //触发票据刷新事件
        this.postEvent(new SsoServerRefreshTicketEvent(token, curTicket, false, serverTicket));
    }

    /**
     * 刷新客户端票据，只能刷新客户端的Claim信息
     *
     * @param token      token
     * @param clientTicket ticketInfo
     */
    public void refreshClientTicket(String token,String appId, ClientTicket clientTicket) {
        if (StringUtils.isBlank(token) || clientTicket == null || BooleanUtils.isNotTrue(serverProperties.getUseSessionStore())) {
            return;
        }

        ServerTicket serverTicket = sessionStore.getTicket(token);
        if (serverTicket == null) {
            log.warn("试图刷新一个不存在的票据信息，token：" + token);
            return;
        }

        Map<String, Map<String, Object>> appClaims = Optional.ofNullable(serverTicket.getAppClaims()).orElse(new HashMap<>());
        appClaims.put(appId, clientTicket.getClaims());
        serverTicket.setAppClaims(appClaims);

        sessionStore.refreshTicket(token, serverTicket, Duration.ofMinutes(serverProperties.getExpire()));
    }

    /**
     * 从HttpServletRequest中获取token
     *
     * @param request 请求
     * @return token
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
     * @param event 事件
     */
    private void postEvent(SsoEvent event) {
        if (eventBus == null || event == null) {
            return;
        }
        eventBus.post(event);
    }

    /**
     * 移除客户端token对应的Ticket缓存
     */
    private void removeClientCache(String token) {
        Set<String> clientHosts = clientStore.getAll(token);
        if (CollectionUtils.isEmpty(clientHosts)) {
            return;
        }

        for (String clientHost : clientHosts) {
            int retryCount = 1;
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
