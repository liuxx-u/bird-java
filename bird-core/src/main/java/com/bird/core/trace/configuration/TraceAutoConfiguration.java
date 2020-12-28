package com.bird.core.trace.configuration;

import com.bird.core.CoreAutoConfiguration;
import com.bird.core.json.JsonSerializer;
import com.bird.core.trace.TraceableAspect;
import com.bird.core.trace.dispatch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Slf4j
@Configuration
@AutoConfigureAfter(CoreAutoConfiguration.class)
@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
@ConditionalOnProperty(value = "bird.trace.enabled", havingValue = "true", matchIfMissing = true)
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({ITraceLogStore.class, ITraceLogDispatcher.class})
    public ITraceLogStore traceLogStore(JsonSerializer jsonSerializer) {
        return new NullTraceLogStore(jsonSerializer);
    }

    @Bean
    @ConditionalOnMissingBean(ITraceLogDispatcher.class)
    @ConfigurationProperties(prefix = "bird.trace.dispatcher")
    public DefaultTraceDispatcherProperties defaultTraceDispatcherProperties() {
        return new DefaultTraceDispatcherProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ITraceLogDispatcher.class)
    public ITraceLogDispatcher traceLogDispatcher(ITraceLogStore traceLogStore, DefaultTraceDispatcherProperties defaultTraceDispatcherProperties) {
        DefaultTraceLogDispatcher defaultTraceLogDispatcher = new DefaultTraceLogDispatcher(traceLogStore, defaultTraceDispatcherProperties);
        defaultTraceLogDispatcher.init();
        return defaultTraceLogDispatcher;
    }

    @Bean
    public TraceableAspect traceableAspect() {
        return new TraceableAspect();
    }
}
