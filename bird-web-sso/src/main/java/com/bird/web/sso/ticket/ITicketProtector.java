package com.bird.web.sso.ticket;

/**
 * sso票据加密解密器
 * Created by liuxx on 2017/5/18.
 */
public interface ITicketProtector {

    String protect(TicketInfo ticket);

    TicketInfo unProtect(String token);
}
