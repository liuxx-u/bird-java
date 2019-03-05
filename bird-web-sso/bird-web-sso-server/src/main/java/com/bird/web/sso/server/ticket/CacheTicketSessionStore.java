package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.TicketInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JVM缓存实现的Session Store
 * @author liuxx
 * @date 2019/3/5
 */
public class CacheTicketSessionStore implements ITicketSessionStore {

    private Cache<String, TicketInfo> cache;

    public CacheTicketSessionStore(Integer expire){
        cache = CacheBuilder.newBuilder().expireAfterWrite(expire, TimeUnit.MINUTES).build();
    }

    @Override
    public String storeTicket(TicketInfo ticketInfo) {
        String key = UUID.randomUUID().toString();
        cache.put(key,ticketInfo);
        return key;
    }

    @Override
    public TicketInfo getTicket(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public TicketInfo refreshTicket(String key, TicketInfo ticketInfo, long expire) {
        ticketInfo.setLastRefreshTime(new Date());

        Date expireDate = new Date(System.currentTimeMillis() + expire);
        ticketInfo.setExpireTime(expireDate);

        cache.put(key, ticketInfo);
        return ticketInfo;
    }

    @Override
    public void removeTicket(String key) {
        cache.invalidate(key);
    }
}
