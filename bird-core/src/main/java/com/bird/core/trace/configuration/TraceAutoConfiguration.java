package com.bird.core.trace.configuration;

import com.bird.core.trace.dispatch.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Configuration
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({ITraceLogStore.class, ITraceLogDispatcher.class})
    public ITraceLogStore traceLogStore() {
        return new DefaultTraceLogStore();
    }

    @Bean
    @ConditionalOnMissingBean(ITraceLogDispatcher.class)
    @ConfigurationProperties(prefix = "bird.trace")
    public DefaultTraceDispatcherProperties defaultTraceDispatcherProperties() {
        return new DefaultTraceDispatcherProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ITraceLogDispatcher.class)
    public ITraceLogDispatcher traceLogDispatcher(ITraceLogStore traceLogStore, DefaultTraceDispatcherProperties defaultTraceDispatcherProperties) {
        return new DefaultTraceLogDispatcher(traceLogStore, defaultTraceDispatcherProperties);
    }
}
