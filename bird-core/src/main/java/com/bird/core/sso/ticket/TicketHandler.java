package com.bird.core.sso.ticket;

import com.bird.core.sso.SsoAuthorizeManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by liuxx on 2017/5/17.
 */
@Component
public class TicketHandler {

    @Autowired(required = false)
    private TicketProtector protector;

    @Autowired(required = false)
    private SsoAuthorizeManager authorizeManager;

    @Autowired(required = false)
    private TicketSessionStore sessionStore;


    public TicketInfo getTicket(HttpServletRequest request) {
        String cookieName = authorizeManager.getCookieName();
        //先从header中获取token
        String token = request.getHeader(cookieName);
        if (StringUtils.isBlank(token)) {
            //header中没有token,则从cookie中获取
            Cookie[] cookies = request.getCookies();
            if (cookies == null) return null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (StringUtils.isBlank(token)) return null;

        TicketInfo ticketInfo;
        if (sessionStore != null) {
            ticketInfo = sessionStore.getTicket(token);
            if (ticketInfo == null) return null;

            //如果超过一半的有效期，则刷新
            Date now = new Date();
            Date issuedTime = ticketInfo.getLastRefreshTime();
            Date expireTime = ticketInfo.getExpireTime();

            long t1 = now.getTime() - issuedTime.getTime();
            long t2 = expireTime.getTime() - now.getTime();
            if (t1 > t2) {
                sessionStore.refreshTicket(token, ticketInfo, t1 + t2);
            }
        } else {
            ticketInfo = protector.unProtect(token);
        }

        return ticketInfo;
    }
}
