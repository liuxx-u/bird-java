package com.bird.web.common.controller;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author liuxx
 * Created by liuxx on 2017/5/25.
 */
public abstract class AbstractController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 当前的请求
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 当前的响应
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * 从线程中获取当前登录用户的票据信息
     * @return session
     */
    protected BirdSession getSession(){
        return SessionContext.getSession();
    }

    /**
     * 从线程中获取当前登录用户的userId
     *
     * @return userId
     */
    protected String getUserId() {
        return SessionContext.getUserId();
    }

    /**
     * 从线程中获取当前登录用户的租户id
     *
     * @return userId
     */
    protected String getTenantId() {
        return SessionContext.getTenantId();
    }
}
