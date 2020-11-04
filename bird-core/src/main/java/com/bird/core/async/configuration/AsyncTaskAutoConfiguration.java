package com.bird.core.async.configuration;

import com.bird.core.async.AsyncTaskConfigurer;
import com.bird.core.async.AsyncTaskProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * @author liuxx
 * @since 2020/11/4
 */
@Configuration
@EnableConfigurationProperties(AsyncTaskProperties.class)
@ConditionalOnProperty(value = "bird.task.pool.core-pool-size")
public class AsyncTaskAutoConfiguration {

    private final AsyncTaskProperties taskProperties;

    public AsyncTaskAutoConfiguration(AsyncTaskProperties taskProperties) {
        this.taskProperties = taskProperties;
    }

    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncTaskConfigurer(taskProperties);
    }
}
