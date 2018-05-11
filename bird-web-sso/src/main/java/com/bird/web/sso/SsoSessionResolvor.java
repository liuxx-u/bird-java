package com.bird.web.sso;

import com.bird.core.session.BirdSession;
import com.bird.web.common.session.AbstractServletSessionResolvor;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/5/11
 */
public class SsoSessionResolvor extends AbstractServletSessionResolvor {

    @Autowired(required = false)
    private SsoAuthorizeManager authorizeManager;

    @Override
    public BirdSession resolve(HttpServletRequest request) {
        if (authorizeManager == null) return null;

        TicketInfo ticketInfo = authorizeManager.getTicket(request);
        if (ticketInfo == null) return null;

        BirdSession session = new BirdSession();
        session.setUserId(ticketInfo.getUserId());
        session.setTenantId(ticketInfo.getTenantId());
        session.setName(ticketInfo.getName());
        session.setClaims(ticketInfo.getClaims());
        return session;
    }
}
