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
public class SsoClientFetchTicketEvent extends SsoEvent {
    private Boolean success;
    private String errorMsg;

    public SsoClientFetchTicketEvent(String token) {
        super();
        this.setToken(token);
    }

    public void success(TicketInfo ticketInfo) {
        this.setSuccess(true);
        this.setTicketInfo(ticketInfo);
    }

    public void fail(String errorMsg) {
        this.setSuccess(false);
        this.setErrorMsg(errorMsg);
    }
}
