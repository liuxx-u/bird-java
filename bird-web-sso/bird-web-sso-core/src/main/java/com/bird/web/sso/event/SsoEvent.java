package com.bird.web.sso.event;

import com.bird.web.sso.ticket.TicketInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author liuxx
 * @date 2019/3/7
 */
@Getter
@Setter
public abstract class SsoEvent {
    private String token;
    private TicketInfo ticketInfo;
    private Date eventTime;

    public SsoEvent(){
        this.eventTime = new Date();
    }

    public SsoEvent(String token,TicketInfo ticketInfo){
        this.token = token;
        this.ticketInfo = ticketInfo;
        this.eventTime = new Date();
    }
}
