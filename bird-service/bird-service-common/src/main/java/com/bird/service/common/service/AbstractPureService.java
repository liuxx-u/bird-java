package com.bird.service.common.service;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuxx
 * @date 2018/12/10
 */
@SuppressWarnings("all")
public abstract class AbstractPureService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 从线程中获取当前登录用户的票据信息
     * @return
     */
    protected BirdSession getSession(){
        return SessionContext.getSession();
    }

    /**
     * 从线程中获取当前登录用户的userId
     *
     * @return
     */
    protected Long getUserId() {
        BirdSession session = getSession();
        if (session == null) return 0L;
        if (session.getUserId() == null || StringUtils.isBlank(session.getUserId().toString())) return 0L;
        return Long.valueOf(session.getUserId().toString());
    }

    /**
     * 从线程中获取当前登录用户的tenantId
     *
     * @return
     */
    protected Long getTenantId() {
        BirdSession session = getSession();
        if (session == null) return 0L;
        if (session.getTenantId() == null || StringUtils.isBlank(session.getTenantId().toString())) return 0L;
        return Long.valueOf(session.getTenantId().toString());
    }
}
