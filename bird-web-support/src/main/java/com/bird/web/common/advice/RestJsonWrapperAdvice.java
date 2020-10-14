package com.bird.web.common.advice;

import com.bird.core.Result;
import com.bird.core.session.SessionContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author liuxx
 * @date 2019/4/28
 */
@RestControllerAdvice
public class RestJsonWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final static String CALL_HEADER = "call";
    private final static String[] INNER_CALL_HEADER = {"ribbon", "feign"};


    private final String[] wrapperScanPackages;

    public RestJsonWrapperAdvice(String... wrapperScanPackages) {
        this.wrapperScanPackages = wrapperScanPackages;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converter) {
        if (methodParameter.getMethodAnnotation(JsonWrapperIgnore.class) != null || methodParameter.getContainingClass().isAnnotationPresent(JsonWrapperIgnore.class)) {
            return false;
        }

        for (String wrapperPackage : this.wrapperScanPackages) {
            if (methodParameter.getDeclaringClass().getName().startsWith(wrapperPackage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object value, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest request, ServerHttpResponse response) {
        //返回结果时移除当前线程中的session数据
        SessionContext.removeSession();

        HttpHeaders headers = request.getHeaders();
        if (ArrayUtils.contains(INNER_CALL_HEADER, headers.getFirst(CALL_HEADER))) {
            return value;
        }
        if (!(value instanceof Result)) {
            value = Result.success("success", value);
        }
        return value;
    }
}
