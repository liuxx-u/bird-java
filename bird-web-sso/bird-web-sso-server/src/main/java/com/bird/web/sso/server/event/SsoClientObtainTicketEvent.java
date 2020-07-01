package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.ServerTicket;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户端来SsoServer获取Ticket信息时触发的事件
 *
 * @author liuxx
 * @date 2019/8/31
 */
@Getter
@Setter
public class SsoClientObtainTicketEvent extends SsoEvent {

    /**
     * 客户端host
     */
    private String clientHost;
    /**
     * 票据信息
     */
    private ServerTicket serverTicket;

    public SsoClientObtainTicketEvent(String token, ServerTicket serverTicket,String clientHost){
        super(token);
        this.serverTicket = serverTicket;
        this.clientHost = clientHost;
    }
}
