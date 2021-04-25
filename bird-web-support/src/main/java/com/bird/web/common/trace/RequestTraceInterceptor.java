package com.bird.web.common.trace;

import com.bird.core.trace.HttpParam;
import com.bird.core.trace.IGlobalTraceIdProvider;
import com.bird.core.trace.TraceContext;
import com.bird.web.common.interceptor.PathMatchInterceptorAdapter;
import com.bird.web.common.reader.BodyReaderFilter;
import com.bird.web.common.utils.RequestHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author liuxx
 * @since 2020/10/10
 */
@RestControllerAdvice
public class RequestTraceInterceptor extends PathMatchInterceptorAdapter implements ResponseBodyAdvice<Object> {

    private final static String IP_CLAIM = "ip";
    private final static List<MediaType> SUPPORT_RESPONSE_CONTENT_TYPE = new ArrayList<>();

    static {
        SUPPORT_RESPONSE_CONTENT_TYPE.add(MediaType.APPLICATION_JSON);
        SUPPORT_RESPONSE_CONTENT_TYPE.add(MediaType.APPLICATION_XML);
        SUPPORT_RESPONSE_CONTENT_TYPE.add(MediaType.TEXT_PLAIN);
        SUPPORT_RESPONSE_CONTENT_TYPE.add(MediaType.TEXT_XML);
    }

    private final RequestTraceProperties requestTraceProperties;
    private final IGlobalTraceIdProvider globalTraceIdProvider;

    public RequestTraceInterceptor(RequestTraceProperties requestTraceProperties,IGlobalTraceIdProvider globalTraceIdProvider){
        this.requestTraceProperties = requestTraceProperties;
        this.globalTraceIdProvider = globalTraceIdProvider;
    }

    /**
     * 拦截器处理方法，记录请求的trace信息
     * <p>
     * 忽略跨域OPTIONS请求与文件上传请求
     *
     * @param request  request
     * @param response response
     * @param handler  跨域第一次OPTIONS请求时handler为AbstractHandlerMapping.PreFlightHandler，不拦截
     * @return 是否进行下一步处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (super.match(request, requestTraceProperties.getUrlPatterns())) {
            HttpParam httpParam = new HttpParam();

            boolean traceAllHeaders = this.requestTraceProperties.traceAllHeaders();
            Map<String, String> headers = new HashMap<>(16);
            Enumeration<String> headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (traceAllHeaders || ArrayUtils.contains(this.requestTraceProperties.getTraceHeaders(), headerName)) {
                    headers.put(headerName, request.getHeader(headerName));
                }
            }

            httpParam.setHeaders(headers);
            httpParam.setParams(request.getQueryString());
            if (BodyReaderFilter.canReadBody(request)) {
                httpParam.setBody(RequestHelper.getBodyString(request));
            } else if (Objects.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, request.getHeader(HttpHeaders.CONTENT_TYPE))) {
                StringJoiner parameters = new StringJoiner("&");
                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String parameterName = parameterNames.nextElement();
                    parameters.add(parameterName + "=" + request.getParameter(parameterName));
                }
                httpParam.setBody(parameters.toString());
            }

            TraceContext.enter(request.getMethod() + ":" + request.getRequestURI(), new Object[]{httpParam}, null);
            TraceContext.current().setClaim(IP_CLAIM, RequestHelper.getRealIp(request));
            TraceContext.current().setGlobalTraceId(globalTraceIdProvider.globalTraceId());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(TraceContext.current() != null){
            TraceContext.exitAndClear(null);
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return TraceContext.current() != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(SUPPORT_RESPONSE_CONTENT_TYPE.contains(selectedContentType)){
            TraceContext.exitAndClear(body);
        }
        return body;
    }
}
