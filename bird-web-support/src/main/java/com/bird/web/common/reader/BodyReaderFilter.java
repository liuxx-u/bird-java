package com.bird.web.common.reader;

import com.bird.core.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 前置Filter，保留请求中的body数据，可多次读取
 *
 * @author liuxx
 * @date 2019/7/23
 */
public class BodyReaderFilter extends OncePerRequestFilter {

    private final static String BODY_READ_PROPERTY = "bird.web.body-read.enabled";
    private final static String TRUE = "true";
    private final static String MULTIPART_HEAD = "multipart/form-data";
    private final static List<HttpMethod> SUPPORT_HTTP_METHODS = new ArrayList<>();

    static {
        SUPPORT_HTTP_METHODS.add(HttpMethod.POST);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PUT);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PATCH);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (canReadBody(request)) {
            filterChain.doFilter(new BodyReaderHttpServletRequestWrapper(request), response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 是否支持读取body内容
     *
     * @param request 请求
     * @return 是否允许读取body内容
     */
    public static boolean canReadBody(HttpServletRequest request) {
        Environment environment = SpringContextHolder.getBean(Environment.class);
        if (!Objects.equals(TRUE, environment.getProperty(BODY_READ_PROPERTY))) {
            return false;
        }

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        return SUPPORT_HTTP_METHODS.contains(method) && !(request instanceof MultipartHttpServletRequest) && !isMultipart(request);
    }

    private static boolean isMultipart(HttpServletRequest request) {

        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWith(contentType, MULTIPART_HEAD);
    }
}
