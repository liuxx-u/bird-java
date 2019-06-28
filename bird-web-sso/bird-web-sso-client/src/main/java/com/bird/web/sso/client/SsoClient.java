package com.bird.web.sso.client;

import com.bird.web.sso.client.event.SsoClientClearCacheEvent;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.utils.CookieHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Slf4j
public class SsoClient {

    private SsoClientProperties clientProperties;
    private IRemoteTicketHandler ticketHandler;
    private Cache<String, TicketInfo> cache;

    @Autowired(required = false)
    private EventBus eventBus;


    public SsoClient(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler) {
        this.clientProperties = clientProperties;
        this.ticketHandler = ticketHandler;

        cache = CacheBuilder.newBuilder().expireAfterWrite(clientProperties.getCache(), TimeUnit.MINUTES).build();
    }

    /**
     * 解析票据信息
     *
     * @param request
     * @return
     */
    public TicketInfo getTicket(HttpServletRequest request) {
        String token = this.getToken(request);
        return this.getTicket(token);
    }

    /**
     * 根据token获取票据信息
     *
     * @param token
     * @return
     */
    public TicketInfo getTicket(String token) {
        if (StringUtils.isBlank(token)) return null;

        try {
            return cache.get(token, () -> ticketHandler.getTicket(token));
        } catch (Exception e) {
            log.warn("sso 客户端票据读取失败", e);
            return null;
        }
    }

    /**
     * 刷新票据信息
     *
     * @param request    request
     * @param ticketInfo 新的票据信息
     */
    public void refreshTicket(HttpServletRequest request, TicketInfo ticketInfo) {
        String token = this.getToken(request);

        this.refreshTicket(token, ticketInfo);
    }

    /**
     * 刷新票据信息
     *
     * @param token      token
     * @param ticketInfo 新的票据信息
     */
    public void refreshTicket(String token, TicketInfo ticketInfo) {
        if (StringUtils.isBlank(token)) return;

        if (ticketInfo == null) {
            log.warn("刷新的票据为null，token:" + token);
            return;
        }
        if (!ticketHandler.refreshTicket(token, ticketInfo)) {
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
        if (StringUtils.isBlank(token)) return;

        TicketInfo ticketInfo = cache.getIfPresent(token);
        cache.invalidate(token);

        this.postEvent(new SsoClientClearCacheEvent(token, ticketInfo));
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
     * @param event
     */
    private void postEvent(SsoEvent event) {
        if (eventBus == null || event == null) return;
        eventBus.post(event);
    }
}
