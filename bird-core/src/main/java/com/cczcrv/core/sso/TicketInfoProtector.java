package com.bird.core.sso;

/**
 * sso票据加密解密器
 * Created by liuxx on 2017/5/18.
 */
public interface TicketInfoProtector {

    String Protect(TicketInfo ticket);

    TicketInfo UnProtect(String token);
}
