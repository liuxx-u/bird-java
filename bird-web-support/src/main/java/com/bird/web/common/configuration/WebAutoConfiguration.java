package com.bird.web.common.configuration;

import com.bird.core.trace.IGlobalTraceIdProvider;
import com.bird.web.common.WebProperties;
import com.bird.web.common.advice.GlobalExceptionAdvice;
import com.bird.web.common.advice.RestJsonWrapperAdvice;
import com.bird.web.common.cors.CorsFilter;
import com.bird.web.common.cors.CorsProperties;
import com.bird.web.common.idempotency.IdempotencyController;
import com.bird.web.common.idempotency.IdempotencyInterceptor;
import com.bird.web.common.reader.BodyReaderFilter;
import com.bird.web.common.security.ip.DefaultIpListProvider;
import com.bird.web.common.security.ip.IIpListProvider;
import com.bird.web.common.security.ip.IpCheckInterceptor;
import com.bird.web.common.trace.RequestTraceInterceptor;
import com.bird.web.common.trace.RequestTraceProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author liuxx
 * @since 2020/8/31
 */
@Configuration
@EnableConfigurationProperties({WebProperties.class, RequestTraceProperties.class})
public class WebAutoConfiguration {

    private final static String PREFIX = "bird.web.";

    private WebProperties webProperties;

    public WebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    /**
     * 注册 统一的响应结果处理组件
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "global-result-wrapper", havingValue = "true", matchIfMissing = true)
    public RestJsonWrapperAdvice restJsonWrapperAdvice() {
        return new RestJsonWrapperAdvice(this.webProperties.getBasePackages());
    }

    /**
     * 注册 统一的异常处理组件
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "global-exception-enabled", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }

    /**
     * 注册 幂等性操作码接口
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enabled", havingValue = "true")
    public IdempotencyController idempotencyController() {
        return new IdempotencyController(webProperties.getIdempotency());
    }

    /**
     * 注册 幂等性校验拦截器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enabled", havingValue = "true")
    public IdempotencyInterceptor idempotencyInterceptor() {
        return new IdempotencyInterceptor(webProperties.getIdempotency());
    }

    /**
     * 注册 跨域资源共享过滤器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "cors.enabled", havingValue = "true")
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsProperties corsProperties = webProperties.getCors();

        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter(corsProperties));
        registration.addUrlPatterns(corsProperties.getUrlPatterns());
        registration.setName("corsFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 注册 请求Body重复读取过滤器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "body-read.enabled", havingValue = "true")
    public FilterRegistrationBean<BodyReaderFilter> bodyReaderFilterRegistration() {

        FilterRegistrationBean<BodyReaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BodyReaderFilter());
        registration.addUrlPatterns("/**");
        registration.setName("bodyReaderFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * 注册 默认的ip配置提供者
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "ip-check.enabled", havingValue = "true")
    @ConditionalOnMissingBean(IIpListProvider.class)
    public IIpListProvider ipListProvider() {
        return new DefaultIpListProvider(this.webProperties.getIpCheck().getIpList());
    }

    /**
     * 注册 ip校验拦截器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "ip-check.enabled", havingValue = "true")
    public IpCheckInterceptor ipCheckInterceptor(IIpListProvider ipListProvider) {
        return new IpCheckInterceptor(ipListProvider);
    }

    /**
     * 注册 默认的globalTraceId提供器
     */
    @Bean
    @ConditionalOnMissingBean(IGlobalTraceIdProvider.class)
    public IGlobalTraceIdProvider defaultGlobalTraceIdProvider() {
        return () -> StringUtils.EMPTY;
    }

    /**
     * 注册 Trace信息拦截器
     */
    @Bean
    @ConditionalOnProperty(value = "bird.trace.request.trace-type", havingValue = "default")
    public RequestTraceInterceptor traceInterceptor(RequestTraceProperties requestTraceProperties, IGlobalTraceIdProvider globalTraceIdProvider) {
        return new RequestTraceInterceptor(requestTraceProperties, globalTraceIdProvider);
    }
}
