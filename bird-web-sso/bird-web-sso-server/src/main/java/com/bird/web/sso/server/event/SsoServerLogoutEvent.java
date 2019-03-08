package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.TicketInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/3/7
 */
@Getter
@Setter
public class SsoServerLogoutEvent extends SsoEvent {

    public SsoServerLogoutEvent(String token, TicketInfo ticketInfo){
        super(token,ticketInfo);
    }
}
