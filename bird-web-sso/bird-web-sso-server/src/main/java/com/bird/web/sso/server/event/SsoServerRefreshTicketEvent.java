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
public class SsoServerRefreshTicketEvent extends SsoEvent {
    private Boolean autoRefresh;
    private TicketInfo newTicketInfo;

    public SsoServerRefreshTicketEvent(String token, TicketInfo ticketInfo, Boolean autoRefresh, TicketInfo newTicketInfo) {
        super(token, ticketInfo);
        this.autoRefresh = autoRefresh;
        this.newTicketInfo = newTicketInfo;
    }
}
