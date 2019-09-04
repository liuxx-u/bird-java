package com.bird.web.sso.client.cache;

import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.ticket.TicketInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2019/9/4
 */
public class DefaultClientTicketCache implements IClientTicketCache {

    private IRemoteTicketHandler ticketHandler;

    private Cache<String, TicketInfo> cache;

    public DefaultClientTicketCache(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler){
        this.ticketHandler = ticketHandler;

        cache = CacheBuilder.newBuilder().expireAfterWrite(clientProperties.getCache(), TimeUnit.MINUTES).build();
    }

    /**
     * 根据token获取TicketInfo
     *
     * @param token token
     * @return ticket
     */
    @Override
    public TicketInfo get(String token) {
        if (StringUtils.isBlank(token)) return null;

        try {
            return cache.get(token, () -> ticketHandler.getTicket(token));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 移除token的客户端缓存
     *
     * @param token token
     */
    @Override
    public TicketInfo remove(String token) {
        if (StringUtils.isBlank(token)) return null;

        TicketInfo ticketInfo = cache.getIfPresent(token);
        cache.invalidate(token);
        return ticketInfo;
    }
}
