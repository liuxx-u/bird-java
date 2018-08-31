package com.bird.web.sso.ticket;

import com.bird.core.Constant;
import com.bird.core.cache.CacheHelper;

import java.util.Date;
import java.util.UUID;

/**
 * redis session存储器
 * @author liuxx
 */
public class RedisTicketSessionStore implements ITicketSessionStore {
    private final String Ticket_CacheKey_Prefix = Constant.Cache.NAMESPACE + "Sso-Token:";

    /**
     * 存储票据信息，并返回该票据的Key
     *
     * @param ticketInfo
     * @return
     */
    @Override
    public String storeTicket(TicketInfo ticketInfo) {

        String key = UUID.randomUUID().toString();
        String cacheKey = Ticket_CacheKey_Prefix + key;
        long span = ticketInfo.getExpireTime().getTime() - ticketInfo.getLastRefreshTime().getTime();
        CacheHelper.getCache().set(cacheKey, ticketInfo, (int) (span / 1000));
        return key;
    }

    /**
     * 根据Key获取票据信息
     *
     * @param key 票据的Key
     * @return
     */
    @Override
    public TicketInfo getTicket(String key) {
        String cacheKey = Ticket_CacheKey_Prefix + key;
        Object ticket = CacheHelper.getCache().get(cacheKey);
        if (ticket instanceof TicketInfo) {
            return (TicketInfo) ticket;
        }
        return null;
    }

    /**
     * 刷新票据
     *
     * @param key
     * @param ticketInfo 票据
     * @param expire 有效期（毫秒）
     */
    @Override
    public void refreshTicket(String key, TicketInfo ticketInfo,long expire) {
        ticketInfo.setLastRefreshTime(new Date());

        Date expireDate = new Date(System.currentTimeMillis() + expire);
        ticketInfo.setExpireTime(expireDate);

        String cacheKey = Ticket_CacheKey_Prefix + key;

        long span = ticketInfo.getExpireTime().getTime() - ticketInfo.getLastRefreshTime().getTime();
        CacheHelper.getCache().set(cacheKey, ticketInfo, (int) (span / 1000));
    }

    /**
     * 移除票据信息
     *
     * @param key
     */
    @Override
    public void removeTicket(String key) {
        String cacheKey = Ticket_CacheKey_Prefix + key;
        CacheHelper.getCache().del(cacheKey);
    }
}
