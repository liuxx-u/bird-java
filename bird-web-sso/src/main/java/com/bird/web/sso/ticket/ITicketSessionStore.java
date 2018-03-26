package com.bird.web.sso.ticket;

public interface ITicketSessionStore {

    /**
     * 存储票据信息，并返回该票据的Key
     * @param ticketInfo
     * @return
     */
    String storeTicket(TicketInfo ticketInfo);

    /**
     * 根据Key获取票据信息
     * @param key 票据的Key
     * @return
     */
    TicketInfo getTicket(String key);

    /**
     * 刷新票据
     *
     * @param key
     * @param ticketInfo 票据
     * @param expire 有效期（毫秒）
     */
    void refreshTicket(String key, TicketInfo ticketInfo,long expire);

    /**
     * 移除票据信息
     * @param key
     */
    void  removeTicket(String key);
}
