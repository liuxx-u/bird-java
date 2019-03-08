package com.bird.web.sso.client.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.TicketInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/3/8
 */
@Getter
@Setter
public class SsoClientClearCacheEvent extends SsoEvent {

    public SsoClientClearCacheEvent(String token, TicketInfo ticketInfo) {
        super(token, ticketInfo);
    }
}
