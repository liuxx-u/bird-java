package com.bird.web.sso.ticket;

/**
 * sso票据加密解密器
 *
 * @author liuxx
 * @date 2017/5/18
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
