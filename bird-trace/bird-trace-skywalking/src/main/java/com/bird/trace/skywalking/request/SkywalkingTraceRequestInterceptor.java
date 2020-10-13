package com.bird.trace.skywalking.request;

import com.bird.core.SpringContextHolder;
import com.bird.core.trace.HttpParam;
import com.bird.core.trace.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Slf4j
public class SkywalkingTraceRequestInterceptor extends HandlerInterceptorAdapter {

    private final static String BODY_READ_PROPERTY = "bird.web.body-read.enabled";
    private final static String TRUE = "true";
    private final static List<HttpMethod> SUPPORT_HTTP_METHODS = new ArrayList<>();

    static {
        SUPPORT_HTTP_METHODS.add(HttpMethod.POST);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PUT);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PATCH);
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
        HttpParam httpParam = new HttpParam();

        Map<String, String> headers = new HashMap<>(8);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        httpParam.setParams(request.getQueryString());
        httpParam.setHeaders(headers);
        if (this.canReadBody(request)) {
            httpParam.setBody(this.getBodyString(request));
        }
        TraceContext.enter(request.getMethod() + ":" + request.getRequestURI(), new Object[]{httpParam}, null);
        TraceContext.current().setGlobalTraceId(org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //移除线程中trace信息
        TraceContext.exitAndClear(null);
    }

    /**
     * 是否支持读取body内容
     *
     * @param request 请求
     * @return 是否允许读取body内容
     */
    private boolean canReadBody(HttpServletRequest request) {
        Environment environment = SpringContextHolder.getBean(Environment.class);
        if (!Objects.equals(environment.getProperty(BODY_READ_PROPERTY), TRUE)) {
            return false;
        }

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        return SUPPORT_HTTP_METHODS.contains(method) && !(request instanceof MultipartHttpServletRequest);
    }

    /**
     * 获取请求Body
     *
     * @param request 请求
     * @return string
     */
    private String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();

        try (
                InputStream inputStream = request.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }
}
