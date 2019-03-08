package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.TicketInfo;

/**
 * 定义票据加密解密器
 *
 * @author liuxx
 * @date 2019/3/1
 */
public interface ITicketProtector {

    /**
     * 票据加密
     * @param ticket
     * @return
     */
    String protect(TicketInfo ticket);

    /**
     * 票据解密
     * @param token
     * @return
     */
    TicketInfo unProtect(String token);
}
