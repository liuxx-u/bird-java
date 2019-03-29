package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.TicketInfo;

/**
 * @author liuxx
 * @date 2019/3/5
 */
public class JwtTicketProtector implements ITicketProtector {
    @Override
    public String protect(TicketInfo ticket) {
        return null;
    }

    @Override
    public TicketInfo unProtect(String token) {
        return null;
    }
}
