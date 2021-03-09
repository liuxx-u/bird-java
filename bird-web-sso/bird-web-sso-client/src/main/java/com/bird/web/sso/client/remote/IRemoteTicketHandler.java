package com.bird.web.sso.client.remote;

import com.bird.web.sso.ticket.ClientTicket;

/**
 * 远程票据解析器
 * @author liuxx
 * @date 2019/3/4
 */
public interface IRemoteTicketHandler {

    /**
     * 获取票据信息
     *
     * @param token       token
     * @param autoRefresh 是否自动刷新有效期
     * @return ticket
     */
    ClientTicket getTicket(String token, boolean autoRefresh);

    /**
     * 刷新服务端票据信息
     *
     * @param token      token
     * @param ticketInfo 新的票据信息
     * @return 是否刷新成功
     */
    Boolean refreshTicket(String token, ClientTicket ticketInfo);
}
