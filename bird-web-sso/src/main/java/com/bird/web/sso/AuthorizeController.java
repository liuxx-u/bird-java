package com.bird.web.sso;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.web.common.controller.AbstractController;
import com.bird.web.common.session.IServletSessionResolvor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/4/2
 */
public abstract class AuthorizeController extends AbstractController {

    @Autowired(required = false)
    private IServletSessionResolvor sessionResolvor;

    /**
     * 从线程中获取当前登录用户的票据信息
     * @return
     */
    protected BirdSession getSession(){
        return SessionContext.getSession();
    }

    /**
     * 从Http请求中获取当前登录用户的票据信息
     *
     * @param request 请求
     * @return
     */
    protected BirdSession getSession(HttpServletRequest request) {
        if(sessionResolvor == null){
            sessionResolvor = new SsoSessionResolvor();
        }
        return sessionResolvor.resolve(request);
    }

    /**
     * 获取当前登录用户的userId
     */
    protected Long getUserId() {
        BirdSession session = getSession(super.request);
        if (session == null) return 0L;
        if (session.getUserId() == null || StringUtils.isBlank(session.getUserId().toString())) return 0L;
        return Long.valueOf(session.getUserId().toString());
    }
}
