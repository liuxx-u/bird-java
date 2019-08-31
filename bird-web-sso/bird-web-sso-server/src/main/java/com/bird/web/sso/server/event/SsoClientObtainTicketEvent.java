package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.TicketInfo;
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

    public SsoClientObtainTicketEvent(String token, TicketInfo ticketInfo,String clientHost){
        super(token,ticketInfo);
        this.clientHost = clientHost;
    }
}
