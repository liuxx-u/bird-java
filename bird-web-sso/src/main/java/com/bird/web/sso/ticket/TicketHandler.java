package com.bird.web.sso.ticket;

import com.bird.web.common.utils.CookieHelper;
import com.bird.web.sso.SsoAuthorizeManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *
 * @author liuxx
 * @date 2017/5/17
 */
public class TicketHandler {
    private boolean autoRefresh;

    @Autowired(required = false)
    private ITicketProtector protector;

    @Autowired(required = false)
    private ITicketSessionStore sessionStore;

    @Autowired
    private SsoAuthorizeManager authorizeManager;


    public TicketInfo getTicket(HttpServletRequest request) {
        String cookieName = authorizeManager.getCookieName();
        //先从header中获取token
        String token = request.getHeader(cookieName);
        if (StringUtils.isBlank(token)) {
            //header中没有token,则从cookie中获取
            token = CookieHelper.getCookieValue(request, cookieName);
        }
        if (StringUtils.isBlank(token)) return null;

        TicketInfo ticketInfo;
        if (authorizeManager.getUseSessionStore()) {
            ticketInfo = sessionStore.getTicket(token);
            if (ticketInfo == null) return null;

            if (autoRefresh) {
                //如果超过一半的有效期，则刷新
                Date now = new Date();
                Date issuedTime = ticketInfo.getLastRefreshTime();
                Date expireTime = ticketInfo.getExpireTime();

                long t1 = now.getTime() - issuedTime.getTime();
                long t2 = expireTime.getTime() - now.getTime();
                if (t1 > t2) {
                    sessionStore.refreshTicket(token, ticketInfo, t1 + t2);
                }
            }
        } else {
            ticketInfo = protector.unProtect(token);
        }

        return ticketInfo;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
}
