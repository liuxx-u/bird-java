package com.bird.web.common.security.signature;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 前置Filter，保留请求中的body数据，可多次读取
 *
 * @author liuxx
 * @date 2019/7/23
 */
public class BodyReaderFilter extends OncePerRequestFilter {

    final static List<HttpMethod> SUPPORT_HTTP_METHODS = new ArrayList<>();

    static {
        SUPPORT_HTTP_METHODS.add(HttpMethod.POST);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PUT);
        SUPPORT_HTTP_METHODS.add(HttpMethod.PATCH);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        if(SUPPORT_HTTP_METHODS.contains(method)){
            filterChain.doFilter(new BodyReaderHttpServletRequestWrapper(request), response);
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
