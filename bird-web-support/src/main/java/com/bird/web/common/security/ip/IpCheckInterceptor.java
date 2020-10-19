package com.bird.web.common.security.ip;

import com.bird.web.common.interceptor.PathMatchInterceptorAdapter;
import com.bird.web.common.utils.IpHelper;
import com.bird.web.common.utils.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ip规则校验拦截器
 *
 * @author liuxx
 * @since 2020/9/4
 */
@Slf4j
public class IpCheckInterceptor extends PathMatchInterceptorAdapter {

    private final IIpListProvider ipListProvider;

    public IpCheckInterceptor(IIpListProvider ipListProvider) {
        this.ipListProvider = ipListProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (CollectionUtils.isEmpty(ipListProvider.listIps())) {
            return true;
        }

        boolean isMatchUri = false;
        for (IpConfProperties ipConf : ipListProvider.listIps()) {
            if (super.match(request, ipConf.getUriPattern())) {
                isMatchUri = true;
                String ip = RequestHelper.getRealIp(request);
                for (String ipPattern : ipConf.listIps()) {
                    if (IpHelper.checkIpRange(ipPattern, ip)) {
                        return true;
                    }
                }
            }
        }
        if (!isMatchUri) {
            return true;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "ip访问限制");
        return false;
    }
}
