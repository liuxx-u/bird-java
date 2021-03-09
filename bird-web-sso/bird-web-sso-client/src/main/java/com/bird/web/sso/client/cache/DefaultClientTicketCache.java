package com.bird.web.sso.client.cache;

import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.ticket.ClientTicket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2019/9/4
 */
@Slf4j
public class DefaultClientTicketCache implements IClientTicketCache {

    private long halfCacheMillis;
    private IRemoteTicketHandler ticketHandler;
    private Cache<String, ClientTicket> cache;

    public DefaultClientTicketCache(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler) {
        this.ticketHandler = ticketHandler;
        this.halfCacheMillis = clientProperties.getCache() * 60 * 1000 / 2L;
        cache = CacheBuilder.newBuilder().expireAfterWrite(clientProperties.getCache(), TimeUnit.MINUTES).build();
    }

    /**
     * 根据token获取TicketInfo
     *
     * @param token       token
     * @param autoRefresh 是否自动刷新有效期
     * @return ticket
     */
    @Override
    public ClientTicket get(String token, boolean autoRefresh) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        try {
            ClientTicket ticket = cache.get(token, () -> ticketHandler.getTicket(token, autoRefresh));
            //客户端缓存时间超过一半，重新获取票据
            if (ticket != null) {
                long span = System.currentTimeMillis() - ticket.getCreateTime().getTime();
                if (span > this.halfCacheMillis) {
                    ticket = ticketHandler.getTicket(token, autoRefresh);
                    if (ticket != null) {
                        cache.put(token, ticket);
                    }
                }
            }
            return ticket;
        } catch (CacheLoader.InvalidCacheLoadException ex) {
            return null;
        } catch (Exception e) {
            log.error("客户端获取Ticket出错", e);
            return null;
        }
    }

    /**
     * 移除token的客户端缓存
     *
     * @param token token
     */
    @Override
    public ClientTicket remove(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        ClientTicket clientTicket = cache.getIfPresent(token);
        cache.invalidate(token);
        return clientTicket;
    }
}
