package com.bird.web.sso.client.remote;

import com.bird.web.sso.ticket.TicketInfo;

/**
 * 远程票据解析器
 * @author liuxx
 * @date 2019/3/4
 */
public interface IRemoteTicketHandler {

    /**
     * 获取票据信息
     *
     * @param token token
     * @return ticket
     */
    TicketInfo getTicket(String token);

    /**
     * 刷新服务端票据信息
     *
     * @param token token
     * @param ticketInfo 新的票据信息
     */
    Boolean refreshTicket(String token, TicketInfo ticketInfo);
}
