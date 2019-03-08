package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.TicketInfo;

/**
 * 定义session存储器
 *
 * @author liuxx
 * @date 2019/3/1
 */
public interface ITicketSessionStore {
    /**
     * 存储票据信息，并返回该票据的Key
     * @param ticketInfo 票据信息
     * @return key
     */
    String storeTicket(TicketInfo ticketInfo);

    /**
     * 根据Key获取票据信息
     * @param key 票据的Key
     * @return 票据信息
     */
    TicketInfo getTicket(String key);

    /**
     * 刷新票据
     *
     * @param key key
     * @param ticketInfo 票据
     * @param expire 有效期（毫秒）
     * @return 刷新后的票据信息
     */
    TicketInfo refreshTicket(String key, TicketInfo ticketInfo,long expire);

    /**
     * 移除票据信息
     * @param key key
     */
    void  removeTicket(String key);
}
