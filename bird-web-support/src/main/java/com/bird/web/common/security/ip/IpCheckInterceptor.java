package com.bird.web.common.security.ip;

import com.bird.core.SpringContextHolder;
import com.bird.web.common.utils.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ip规则校验拦截器
 *
 * @author liuxx
 * @since 2020/9/4
 */
@Slf4j
public class IpCheckInterceptor extends HandlerInterceptorAdapter {

    private final IIpListProvider ipListProvider;

    public IpCheckInterceptor(IIpListProvider ipListProvider) {
        this.ipListProvider = ipListProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (CollectionUtils.isEmpty(ipListProvider.listIps())) {
            return true;
        }

        String uri = request.getRequestURI();
        PathMatcher pathMatcher = this.getPathMatcher();
        for (IpConfProperties ipConf : ipListProvider.listIps()) {
            if (pathMatcher.match(ipConf.getUriPattern(), uri)) {
                String ip = RequestHelper.getRealIp(request);
                for (String ipPattern : ipConf.listIps()) {
                    //TODO: Check ip pattern

                }
                return false;
            }
        }

        return true;
    }

    private PathMatcher getPathMatcher() {
        PathMatcher pathMatcher = null;
        try {
            SpringContextHolder.getBean(PathMatcher.class);
        } catch (Exception ex) {
            pathMatcher = new AntPathMatcher();
        }
        return pathMatcher;
    }
}
