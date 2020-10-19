package com.bird.web.common.interceptor;

import com.bird.core.SpringContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @since 2020/10/19
 */
public abstract class PathMatchInterceptorAdapter extends HandlerInterceptorAdapter {

    protected boolean match(HttpServletRequest request, String uriPattern) {
        PathMatcher pathMatcher = getPathMatcher();
        return pathMatcher.match(uriPattern, request.getRequestURI());
    }

    protected boolean match(HttpServletRequest request, String[] uriPatterns) {
        String uri = request.getRequestURI();
        PathMatcher pathMatcher = getPathMatcher();
        for (String uriPattern : uriPatterns) {
            if (pathMatcher.match(uriPattern, uri)) {
                return true;
            }
        }
        return false;
    }

    private PathMatcher getPathMatcher() {
        PathMatcher pathMatcher;
        try {
            pathMatcher = SpringContextHolder.getBean(PathMatcher.class);
        } catch (Exception ex) {
            pathMatcher = new AntPathMatcher();
        }
        return pathMatcher;
    }
}
