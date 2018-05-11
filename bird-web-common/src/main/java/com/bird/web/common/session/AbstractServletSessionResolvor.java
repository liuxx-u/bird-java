package com.bird.web.common.session;

import com.bird.core.session.BirdSession;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/5/11
 */
public abstract class AbstractServletSessionResolvor implements IServletSessionResolvor {

    @Override
    public BirdSession resolve(HttpServletRequest request) {
        return null;
    }
}
