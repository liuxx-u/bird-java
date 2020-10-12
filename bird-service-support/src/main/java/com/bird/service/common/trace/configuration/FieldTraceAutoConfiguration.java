package com.bird.service.common.trace.configuration;

import com.bird.service.common.trace.FieldTraceProperties;
import com.bird.service.common.trace.interceptor.MybatisFieldTraceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 跟踪相关自动化配置类
 * @author shaojie
 */
@Configuration
@EnableConfigurationProperties(FieldTraceProperties.class)
@ConditionalOnProperty(value = "bird.trace.db-field.enabled", havingValue = "true")
public class FieldTraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MybatisFieldTraceInterceptor.class)
    public MybatisFieldTraceInterceptor fieldTraceInterceptor() {
        return new MybatisFieldTraceInterceptor();
    }
}
