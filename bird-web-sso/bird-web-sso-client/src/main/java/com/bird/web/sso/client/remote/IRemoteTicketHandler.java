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
     * 移除票据信息
     *
     * @param token token
     * @param notifyAll 是否通知所有客户端
     */
    void removeTicket(String token, Boolean notifyAll);
}
