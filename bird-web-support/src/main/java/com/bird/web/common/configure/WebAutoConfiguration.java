package com.bird.web.common.configure;

import com.bird.core.SpringContextHolder;
import com.bird.web.common.WebProperties;
import com.bird.web.common.advice.RestJsonWrapperAdvice;
import com.bird.web.common.idempotency.IdempotencyController;
import com.bird.web.common.idempotency.IdempotencyInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * 注册 SpringContextHolder
     */
    @Bean
    public SpringContextHolder springContextHolder(){
        return new SpringContextHolder();
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
     * 注册 幂等性操作码接口
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enable",havingValue = "true",matchIfMissing = true)
    public IdempotencyController idempotencyController(){
        return new IdempotencyController(webProperties.getIdempotency());
    }

    /**
     * 注册 幂等性校验拦截器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "idempotency.enable",havingValue = "true",matchIfMissing = true)
    public IdempotencyInterceptor idempotencyInterceptor(){
        return new IdempotencyInterceptor(webProperties.getIdempotency());
    }
}
