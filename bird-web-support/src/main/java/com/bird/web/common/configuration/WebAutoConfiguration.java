package com.bird.web.common.configuration;

import com.bird.web.common.WebProperties;
import com.bird.web.common.advice.GlobalExceptionAdvice;
import com.bird.web.common.advice.RestJsonWrapperAdvice;
import com.bird.web.common.cors.CorsFilter;
import com.bird.web.common.cors.CorsProperties;
import com.bird.web.common.idempotency.IdempotencyController;
import com.bird.web.common.idempotency.IdempotencyInterceptor;
import com.bird.web.common.security.ip.DefaultIpListProvider;
import com.bird.web.common.security.ip.IIpListProvider;
import com.bird.web.common.security.ip.IpCheckInterceptor;
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
@EnableConfigurationProperties(WebProperties.class)
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
    @ConditionalOnProperty(value = PREFIX + "global-exception-enable", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }

    /**
     * 注册 幂等性操作码接口
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enable", havingValue = "true")
    public IdempotencyController idempotencyController() {
        return new IdempotencyController(webProperties.getIdempotency());
    }

    /**
     * 注册 幂等性校验拦截器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enable", havingValue = "true")
    public IdempotencyInterceptor idempotencyInterceptor() {
        return new IdempotencyInterceptor(webProperties.getIdempotency());
    }

    /**
     * 注册 跨域资源共享过滤器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "cors.enable", havingValue = "true")
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
     * 注册 默认的ip配置提供者
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "ip-check.enable", havingValue = "true")
    @ConditionalOnMissingBean(IIpListProvider.class)
    public IIpListProvider ipListProvider() {
        return new DefaultIpListProvider(this.webProperties.getIpCheck().getIpList());
    }

    /**
     * 注册 ip校验拦截器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "ip-check.enable", havingValue = "true")
    public IpCheckInterceptor ipCheckInterceptor(IIpListProvider ipListProvider) {
        return new IpCheckInterceptor(ipListProvider);
    }
}
