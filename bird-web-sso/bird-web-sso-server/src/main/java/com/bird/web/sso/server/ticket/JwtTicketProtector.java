package com.bird.web.sso.server.ticket;

import com.bird.web.sso.ticket.ServerTicket;

/**
 * @author liuxx
 * @date 2019/3/5
 */
public class JwtTicketProtector implements ITicketProtector {
    @Override
    public String protect(ServerTicket ticket) {
        return null;
    }

    @Override
    public ServerTicket unProtect(String token) {
        return null;
    }
}
