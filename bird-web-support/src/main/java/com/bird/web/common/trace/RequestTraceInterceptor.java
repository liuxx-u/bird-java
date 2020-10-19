package com.bird.web.common.trace;

import com.bird.core.trace.HttpParam;
import com.bird.core.trace.IGlobalTraceIdProvider;
import com.bird.core.trace.TraceContext;
import com.bird.web.common.interceptor.PathMatchInterceptorAdapter;
import com.bird.web.common.reader.BodyReaderFilter;
import com.bird.web.common.utils.RequestHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2020/10/10
 */
public class RequestTraceInterceptor extends PathMatchInterceptorAdapter {

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
        if(super.match(request,requestTraceProperties.getUrlPatterns())){
            HttpParam httpParam = new HttpParam();

            boolean traceAllHeaders = this.requestTraceProperties.traceAllHeaders();
            Map<String, String> headers = new HashMap<>(16);
            Enumeration<String> headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if(traceAllHeaders || ArrayUtils.contains(this.requestTraceProperties.getTraceHeaders(),headerName)){
                    headers.put(headerName, request.getHeader(headerName));
                }
            }

            httpParam.setHeaders(headers);
            httpParam.setParams(request.getQueryString());
            if (BodyReaderFilter.canReadBody(request)) {
                httpParam.setBody(RequestHelper.getBodyString(request));
            }
            TraceContext.enter(request.getMethod() + ":" + request.getRequestURI(), new Object[]{httpParam}, null);
            TraceContext.current().setGlobalTraceId(globalTraceIdProvider.globalTraceId());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //移除线程中trace信息
        TraceContext.exitAndClear(null);
    }
}
