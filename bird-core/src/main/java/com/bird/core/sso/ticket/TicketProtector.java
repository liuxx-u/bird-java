package com.bird.core.sso.ticket;

import com.bird.core.sso.ticket.TicketInfo;

/**
 * sso票据加密解密器
 * Created by liuxx on 2017/5/18.
 */
public interface TicketProtector {

    String protect(TicketInfo ticket);

    TicketInfo unProtect(String token);
}
