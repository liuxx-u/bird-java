package com.bird.web.sso;

import com.bird.web.common.controller.AbstractController;
import com.bird.web.sso.ticket.TicketHandler;
import com.bird.web.sso.ticket.TicketInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/4/2
 */
public abstract class AuthorizeController extends AbstractController {
    @Autowired
    protected SsoAuthorizeManager authorizeManager;

    /**
     * 获取当前登录用户的票据信息
     * @param request 请求
     * @return
     */
    protected TicketInfo getUser(HttpServletRequest request) {
        return authorizeManager.getTicket(request);
    }

    /**
     * 获取当前登录用户的userId
     * @param request 请求
     * @return
     */
    protected Long getUserId(HttpServletRequest request) {
        TicketInfo ticketInfo = getUser(request);
        if (ticketInfo == null) return 0L;
        if (StringUtils.isBlank(ticketInfo.getUserId())) return 0L;
        return Long.valueOf(ticketInfo.getUserId());
    }
}
