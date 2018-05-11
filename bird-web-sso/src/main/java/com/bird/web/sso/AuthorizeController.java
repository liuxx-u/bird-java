package com.bird.web.sso;

import com.bird.core.session.BirdSession;
import com.bird.web.common.controller.AbstractController;
import com.bird.web.common.session.IServletSessionResolvor;
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
    private IServletSessionResolvor sessionResolvor;

    /**
     * 获取当前登录用户的票据信息
     *
     * @param request 请求
     * @return
     */
    protected BirdSession getSession(HttpServletRequest request) {
        return sessionResolvor.resolve(request);
    }

    /**
     * 获取当前登录用户的userId
     *
     * @param request 请求
     * @return
     */
    protected Long getUserId(HttpServletRequest request) {
        BirdSession session = getSession(request);
        if (session == null) return 0L;
        if (session.getUserId() == null || StringUtils.isBlank(session.getUserId().toString())) return 0L;
        return Long.valueOf(session.getUserId().toString());
    }
}
