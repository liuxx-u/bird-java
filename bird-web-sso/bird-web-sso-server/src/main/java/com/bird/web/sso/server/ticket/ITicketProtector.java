package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.ServerTicket;

/**
 * 定义票据加密解密器
 *
 * @author liuxx
 * @date 2019/3/1
 */
public interface ITicketProtector {

    /**
     * 票据加密
     * @param ticket 票据信息
     * @return 加密后字符串
     */
    String protect(ServerTicket ticket);

    /**
     * 票据解密
     * @param token token
     * @return 票据信息
     */
    ServerTicket unProtect(String token);
}
