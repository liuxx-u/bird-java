package com.bird.web.common.interceptor;

import com.bird.web.common.interceptor.support.IIpAddressChecker;
import com.bird.web.common.utils.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ip地址检查拦截器
 *
 * @author liuxx
 * @date 2018/4/18
 */
public class IpAddressInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IIpAddressChecker ipChecker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ipChecker == null) return true;

        String ip = RequestHelper.getRealIp(request);
        return ipChecker.check(ip);
    }
}
