package com.bird.web.sso.client;

import com.bird.web.sso.client.cache.IClientTicketCache;
import com.bird.web.sso.client.event.SsoClientClearCacheEvent;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.ClientTicket;
import com.bird.web.sso.utils.CookieHelper;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Slf4j
public class SsoClient {

    private SsoClientProperties clientProperties;
    private IRemoteTicketHandler ticketHandler;
    private IClientTicketCache clientTicketCache;

    @Autowired(required = false)
    private EventBus eventBus;


    public SsoClient(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler,IClientTicketCache clientTicketCache) {
        this.clientProperties = clientProperties;
        this.ticketHandler = ticketHandler;
        this.clientTicketCache = clientTicketCache;
    }

    /**
     * 解析票据信息
     *
     * @param request request
     * @return TicketInfo
     */
    public ClientTicket getTicket(HttpServletRequest request) {
        String token = this.getToken(request);
        return this.getTicket(token);
    }

    /**
     * 根据token获取票据信息
     *
     * @param token token
     * @return TicketInfo
     */
    public ClientTicket getTicket(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        return clientTicketCache.get(token);
    }

    /**
     * 刷新票据信息
     *
     * @param request    request
     * @param clientTicket 新的票据信息
     */
    public void refreshTicket(HttpServletRequest request, ClientTicket clientTicket) {
        String token = this.getToken(request);

        this.refreshTicket(token, clientTicket);
    }

    /**
     * 刷新票据信息
     *
     * @param token      token
     * @param clientTicket 新的票据信息
     */
    public void refreshTicket(String token, ClientTicket clientTicket) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        if (clientTicket == null) {
            log.warn("刷新的票据为null，token:" + token);
            return;
        }
        if (!ticketHandler.refreshTicket(token, clientTicket)) {
            log.error("票据刷新失败");
            return;
        }
        this.removeTicketCache(token);
    }

    /**
     * 移除客户端票据缓存
     *
     * @param request request
     */
    public void removeTicketCache(HttpServletRequest request) {
        String token = this.getToken(request);
        this.removeTicketCache(token);
    }

    /**
     * 移除客户端票据缓存
     *
     * @param token token
     */
    public void removeTicketCache(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        ClientTicket clientTicket = clientTicketCache.remove(token);

        this.postEvent(new SsoClientClearCacheEvent(token, clientTicket));
    }

    /**
     * 从Request中获取token
     *
     * @param request request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(clientProperties.getCookieName());
        if (StringUtils.isBlank(token)) {
            //header中没有token,则从cookie中获取
            token = CookieHelper.getCookieValue(request, clientProperties.getCookieName());
        }
        return token;
    }

    /**
     * 触发事件
     *
     * @param event event
     */
    private void postEvent(SsoEvent event) {
        if (eventBus == null || event == null) {
            return;
        }
        eventBus.post(event);
    }
}
