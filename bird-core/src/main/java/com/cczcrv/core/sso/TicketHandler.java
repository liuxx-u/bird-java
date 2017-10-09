package com.bird.core.sso;

import com.bird.core.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuxx on 2017/5/17.
 */

@Component
public class TicketHandler {

    @Autowired(required = false)
    private TicketInfoProtector protector;

    public TicketInfo getTicket(HttpServletRequest request) {
        String cookieName = "sso.token";
        //先从header中获取token
        String token = request.getHeader(cookieName);
        if (!StringHelper.isNullOrWhiteSpace(token)) {
            return decryptToken(token);
        }
        //header中没有token,则从cookie中获取
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                token = cookie.getValue();
                return decryptToken(token);
            }
        }

        return null;
    }

    public TicketInfo decryptToken(String token) {
        return protector.UnProtect(token);
    }
}
