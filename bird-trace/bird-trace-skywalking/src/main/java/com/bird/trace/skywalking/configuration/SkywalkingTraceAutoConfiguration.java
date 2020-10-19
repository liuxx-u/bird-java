package com.bird.trace.skywalking.configuration;

import com.bird.core.trace.dispatch.ITraceLogDispatcher;
import com.bird.trace.skywalking.dispatcher.SkywalkingTraceLogDispatcher;
import com.bird.trace.skywalking.request.SkywalkingGlobalTraceIdProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Configuration
@AutoConfigureBefore(name = {"com.bird.core.trace.configuration.TraceAutoConfiguration","com.bird.web.common.configuration.WebAutoConfiguration"})
public class SkywalkingTraceAutoConfiguration {

    @Bean
    public ITraceLogDispatcher traceLogDispatcher() {
        return new SkywalkingTraceLogDispatcher();
    }

    @Bean
    @ConditionalOnProperty(value = "bird.trace.request.trace-type", havingValue = "skywalking")
    public SkywalkingGlobalTraceIdProvider skywalkingGlobalTraceIdProvider() {
        return new SkywalkingGlobalTraceIdProvider();
    }
}
