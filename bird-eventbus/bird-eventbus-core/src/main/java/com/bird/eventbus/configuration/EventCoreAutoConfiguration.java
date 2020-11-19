package com.bird.eventbus.configuration;

import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.handler.*;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.registry.MapEventRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Configuration
@EnableConfigurationProperties(EventHandlerProperties.class)
@ConditionalOnProperty(value = EventbusConstant.Handler.SCAN_PACKAGES)
@AutoConfigureAfter(name = "org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor")
public class EventCoreAutoConfiguration {

    private final EventHandlerProperties handlerProperties;

    public EventCoreAutoConfiguration(EventHandlerProperties handlerProperties) {
        this.handlerProperties = handlerProperties;
    }

    /**
     * 注册 事件处理方法执行线程池
     */
    @Bean
    @ConditionalOnMissingBean(IEventMethodExecutor.class)
    public IEventMethodExecutor eventMethodExecutor(ObjectProvider<ThreadPoolTaskExecutor> taskExecutor) {
        return new DefaultEventMethodExecutor(taskExecutor.getIfAvailable(ThreadPoolTaskExecutor::new));
    }

    /**
     * 注册 事件处理方法注册中心
     */
    @Bean

    @ConditionalOnMissingBean(IEventRegistry.class)
    public IEventRegistry eventRegistry() {
        return new MapEventRegistry();
    }

    /**
     * 注册 事件处理方法初始化器
     */
    @Bean
    public EventMethodInitializer eventMethodInitializer(IEventRegistry eventRegistry, ObjectProvider<IEventMethodInitializeListener> initializeListener) {
        EventMethodInitializer eventMethodInitializer = new EventMethodInitializer(this.handlerProperties, eventRegistry, initializeListener.getIfAvailable());
        eventMethodInitializer.initialize();
        return eventMethodInitializer;
    }

    /**
     * 注册 事件处理方法执行器
     */
    @Bean
    public EventMethodInvoker eventMethodInvoker(IEventMethodExecutor eventMethodExecutor, IEventRegistry eventRegistry, ObjectProvider<List<IEventMethodInvokerInterceptor>> invokerInterceptors) {
        return new EventMethodInvoker(this.handlerProperties, eventMethodExecutor, eventRegistry, invokerInterceptors.getIfAvailable());
    }
}
