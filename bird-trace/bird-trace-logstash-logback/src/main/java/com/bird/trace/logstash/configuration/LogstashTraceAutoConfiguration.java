package com.bird.trace.logstash.configuration;

import com.bird.core.trace.dispatch.ITraceLogDispatcher;
import com.bird.trace.logstash.LogstashTraceLogDispatcher;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2021/4/14
 */
@Configuration
@AutoConfigureBefore(name = {"com.bird.core.trace.configuration.TraceAutoConfiguration","com.bird.web.common.configuration.WebAutoConfiguration"})
public class LogstashTraceAutoConfiguration {

    @Bean
    public ITraceLogDispatcher traceLogDispatcher() {
        return new LogstashTraceLogDispatcher();
    }
}
