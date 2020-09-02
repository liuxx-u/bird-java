package com.bird.web.common.cors;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * CORS过滤器，支持设置Cookie
 *
 * @author liuxx
 * @date 2020/9/2
 */
public class CorsFilter extends OncePerRequestFilter {

    private final CorsProperties corsProperties;

    public CorsFilter(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String allowOrigin = this.corsProperties.getAllowOrigin();
        if (Objects.equals(allowOrigin, "default")) {
            allowOrigin = request.getHeader("Origin");
        }
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", this.corsProperties.getAllowMethods());
        response.addHeader("Access-Control-Allow-Headers", this.corsProperties.getAllowHeaders());
        response.setHeader("Access-Control-Max-Age", this.corsProperties.getMaxAge());
        if (Objects.equals(this.corsProperties.getAllowCredentials(), "true")) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }

        filterChain.doFilter(request, response);
    }
}
