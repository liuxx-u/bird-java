package com.bird.web.sso.client.cache;

import com.bird.web.sso.ticket.ClientTicket;

/**
 * 客户端票据缓存
 *
 * 缓存未命中时需去sso服务器获取Ticket并缓存
 *
 * @author liuxx
 * @date 2019/9/4
 */
public interface IClientTicketCache {

    /**
     * 根据token获取TicketInfo
     * <p>
     * 先从缓存获取，本地缓存没有则从sso服务端获取
     *
     * @param token       token
     * @param autoRefresh 是否自动刷新有效期
     * @return ticket
     */
    ClientTicket get(String token, boolean autoRefresh);

    /**
     * 移除token的客户端缓存
     *
     * @param token token
     * @return 客户端票据信息
     */
    ClientTicket remove(String token);
}
