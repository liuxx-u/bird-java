package com.bird.trace.client.configure;

import com.bird.trace.client.aspect.TraceableAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2019/8/5
 */
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
@ConditionalOnProperty(value = "bird.trace.client.enabled", matchIfMissing = true)
public class TraceClientAutoConfigurer {

    /**
     * 日志拦截切面
     * @return 切面
     */
    @Bean
    public TraceableAspect traceableAspect(){
        return new TraceableAspect();
    }
}
