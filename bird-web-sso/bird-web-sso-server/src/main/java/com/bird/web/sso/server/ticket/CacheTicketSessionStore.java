package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.ServerTicket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JVM缓存实现的Session Store
 * @author liuxx
 * @date 2019/3/5
 */
public class CacheTicketSessionStore implements ITicketSessionStore {

    private Cache<String, ServerTicket> cache;

    public CacheTicketSessionStore(Integer expire) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expire, TimeUnit.MINUTES).build();
    }

    @Override
    public String storeTicket(ServerTicket serverTicket) {
        String key = UUID.randomUUID().toString();
        cache.put(key, serverTicket);
        return key;
    }

    @Override
    public ServerTicket getTicket(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public ServerTicket refreshTicket(String key, ServerTicket ticketInfo, Duration duration) {
        ticketInfo.setLastRefreshTime(new Date());

        Date expireDate = new Date(System.currentTimeMillis() + duration.toMillis());
        ticketInfo.setExpireTime(expireDate);

        cache.put(key, ticketInfo);
        return ticketInfo;
    }

    @Override
    public void removeTicket(String key) {
        cache.invalidate(key);
    }
}
