package com.bird.web.sso.client.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.ClientTicket;
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
    private ClientTicket clientTicket;

    public SsoClientFetchTicketEvent(String token) {
        super();
        this.setToken(token);
    }

    public void success(ClientTicket clientTicket) {
        this.setSuccess(true);
        this.clientTicket = clientTicket;
    }

    public void fail(String errorMsg) {
        this.setSuccess(false);
        this.setErrorMsg(errorMsg);
    }
}
