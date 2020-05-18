package com.bird.service.common.service;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public abstract class BaseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 从线程中获取当前登录用户信息
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
    protected <T extends Serializable> T getUserId() {
        return SessionContext.getUserId();
    }

    /**
     * 从线程中获取当前登录用户的租户id
     *
     * @return userId
     */
    protected <T extends Serializable> T getTenantId() {
        return SessionContext.getTenantId();
    }
}
