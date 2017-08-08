package com.cczcrv.core.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuxx on 2017/5/18.
 */
public class SsoAuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TicketHandler ticketHandler;

    @Autowired
    private SsoAuthorizeConfig ssoConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return false;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(SsoAuthorize.class)) {
            TicketInfo ticketInfo = ticketHandler.getTicket(request);
            if (ticketInfo == null) {
                //TODO 跳转至登陆页
                return false;
            }
        }

        return true;
    }
}
