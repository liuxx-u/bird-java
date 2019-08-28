package com.bird.web.common.session;

import com.bird.core.session.BirdSession;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * Servlet Session解析器
 */
public interface IServletSessionResolver {

    /**
     * 从HttpServletRequest中解析Session
     * @param request request
     * @return session
     */
    BirdSession resolve(HttpServletRequest request);
}
