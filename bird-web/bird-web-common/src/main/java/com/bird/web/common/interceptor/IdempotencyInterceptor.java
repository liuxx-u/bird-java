package com.bird.web.common.interceptor;

import com.bird.core.cache.CacheHelper;
import com.bird.web.common.WebConstant;
import com.bird.web.common.interceptor.support.Idempotency;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 幂等性处理拦截器
 *
 * @author liuxx
 * @date 2018/7/23
 */
public class IdempotencyInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${bird.web.idempotency.header:bird-idempotency}")
    private String headerName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Idempotency idempotency = handlerMethod.getMethodAnnotation(Idempotency.class);
        if (idempotency == null) return true;

        String token = request.getHeader(this.headerName);
        if (StringUtils.isBlank(token)) {
            logger.warn("幂等性接口：{}，请求头中token为空.", request.getRequestURI());
            if (idempotency.force()) {
                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "该操作已失效，请刷新后重试");
                return false;
            }
            return true;
        }
        if (!CacheHelper.getCache().del(WebConstant.Cache.IDEMPOTENCY_NAMESPACE + token)) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "该操作已提交");
            return false;
        }

        return true;
    }
}
