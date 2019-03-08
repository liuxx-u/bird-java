package com.bird.web.sso.client.sample.interceptor;

import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@Component
public class SsoClientAuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SsoClient ssoClient;

    /**
     * 拦截器处理方法
     *
     * @param request  request
     * @param response response
     * @param handler  跨域第一次OPTIONS请求时handler为AbstractHandlerMapping.PreFlightHandler，不拦截
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        TicketInfo ticketInfo = ssoClient.getTicket(request);

        // 实现业务系统自己的验证逻辑

        return true;
    }
}
