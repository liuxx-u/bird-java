package com.bird.web.common.idempotency;

import com.bird.web.common.WebConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
@Slf4j
public class IdempotencyInterceptor extends HandlerInterceptorAdapter {

    private IdempotencyProperties idempotencyProperties;

    public IdempotencyInterceptor(IdempotencyProperties idempotencyProperties) {
        this.idempotencyProperties = idempotencyProperties;
    }

    @Autowired(required = false)
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Idempotency idempotency = handlerMethod.getMethodAnnotation(Idempotency.class);
        if (idempotency == null) {
            return true;
        }

        String token = request.getHeader(idempotencyProperties.getHeaderName());
        if (StringUtils.isBlank(token)) {
            log.warn("幂等性接口：{}，请求头中token为空.", request.getRequestURI());
            if (idempotency.force()) {
                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, idempotencyProperties.getErrorMessage());
                return false;
            }
            return true;
        }
        if (redisTemplate == null) {
            log.warn("幂等性校验，RedisTemplate未注入");
            return true;
        }
        if (BooleanUtils.isNotTrue(redisTemplate.delete(WebConstant.Cache.IDEMPOTENCY + token))) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, idempotencyProperties.getErrorMessage());
            return false;
        }
        return true;
    }
}
