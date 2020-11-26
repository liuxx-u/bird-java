package com.bird.eventbus.configuration;

import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.handler.*;
import com.bird.eventbus.log.*;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.registry.MapEventRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = {EventbusConstant.Handler.GROUP})
@EnableConfigurationProperties(EventHandlerProperties.class)
public class EventHandlerAutoConfiguration {

    private final EventHandlerProperties handlerProperties;

    public EventHandlerAutoConfiguration(EventHandlerProperties handlerProperties) {
        this.handlerProperties = handlerProperties;
    }

    /**
     * 注册 事件处理方法执行线程池
     */
    @Bean
    @ConditionalOnMissingBean(IEventMethodAsyncConfigurer.class)
    public IEventMethodAsyncConfigurer eventMethodAsyncConfigurer() {
        return new DefaultEventMethodAsyncConfigurer();
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
    public EventMethodInvoker eventMethodInvoker(IEventMethodAsyncConfigurer eventMethodExecutor, IEventRegistry eventRegistry, ObjectProvider<List<IEventMethodInvokerInterceptor>> invokerInterceptors, IEventLogDispatcher eventLogDispatcher) {
        return new EventMethodInvoker(this.handlerProperties, eventMethodExecutor, eventRegistry, invokerInterceptors.getIfAvailable(), eventLogDispatcher);
    }
}
