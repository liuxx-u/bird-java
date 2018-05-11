package com.bird.web.sso;

import com.bird.core.session.BirdSession;
import com.bird.web.common.session.AbstractServletSessionResolvor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/5/11
 */
public class SsoSessionResolvor extends AbstractServletSessionResolvor {

    public static final String SESSION_ATTRIBUTE_KEY = "sso-session";

    @Override
    public BirdSession resolve(HttpServletRequest request) {
        Object session = request.getAttribute(SESSION_ATTRIBUTE_KEY);
        if(session instanceof BirdSession)return (BirdSession)session;
        return null;
    }
}
