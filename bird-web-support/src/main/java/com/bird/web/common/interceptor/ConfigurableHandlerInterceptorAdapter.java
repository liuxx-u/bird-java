package com.bird.web.common.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author liuxx
 * @since 2020/12/9
 */
public abstract class ConfigurableHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    public final static String[] DEFAULT_PATH_PATTERNS = new String[]{"/**"};
    public final static String[] DEFAULT_EXCLUDE_PATH_PATTERNS = new String[]{"/*.ico", "/*.htm", "/*.html", "/*.css", "/*.js", "/*.txt", "/*.json", "/*/api-docs", "/swagger**", "/webjars/**", "/configuration/**"};

    public String[] pathPatterns() {
        return DEFAULT_PATH_PATTERNS;
    }

    public String[] excludePathPatterns() {
        return DEFAULT_EXCLUDE_PATH_PATTERNS;
    }
}
