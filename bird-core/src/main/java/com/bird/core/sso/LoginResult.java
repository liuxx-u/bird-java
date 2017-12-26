package com.bird.core.sso;

import com.bird.core.service.AbstractDTO;
import com.bird.core.sso.ticket.TicketInfo;

public class LoginResult extends AbstractDTO {
    private boolean success;
    private String errorInfo;
    private TicketInfo ticketInfo;

    public static LoginResult Success(TicketInfo ticketInfo) {
        return new LoginResult(true, null, ticketInfo);
    }

    public static LoginResult Error(String errorInfo) {
        return new LoginResult(false, errorInfo, null);
    }

    public LoginResult() {
    }

    public LoginResult(boolean success, String errorInfo, TicketInfo ticketInfo) {
        this.success = success;
        this.errorInfo = errorInfo;
        this.ticketInfo = ticketInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public TicketInfo getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(TicketInfo ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
}
