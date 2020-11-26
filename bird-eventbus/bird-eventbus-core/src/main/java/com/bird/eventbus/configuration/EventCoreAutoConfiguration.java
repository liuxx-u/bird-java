package com.bird.eventbus.configuration;

import com.bird.eventbus.EventBus;
import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.handler.*;
import com.bird.eventbus.log.*;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.registry.MapEventRegistry;
import com.bird.eventbus.sender.IEventSendInterceptor;
import com.bird.eventbus.sender.IEventSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Configuration
@EnableConfigurationProperties({EventHandlerProperties.class, EventLogProperties.class})
@ConditionalOnProperty(value = EventbusConstant.Handler.SCAN_PACKAGES)
public class EventCoreAutoConfiguration {

    private final EventLogProperties logProperties;
    private final EventHandlerProperties handlerProperties;

    public EventCoreAutoConfiguration(EventLogProperties logProperties, EventHandlerProperties handlerProperties) {
        this.logProperties = logProperties;
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
     * 注册 默认的事件日志存储器
     */
    @Bean
    @ConditionalOnMissingBean({IEventSendLogStore.class, IEventHandleLogStore.class, IEventLogDispatcher.class})
    public NullEventLogStore eventLogStore() {
        return new NullEventLogStore();
    }

    /**
     * 注册 默认的事件日志调度器
     */
    @Bean
    @ConditionalOnMissingBean(IEventLogDispatcher.class)
    public IEventLogDispatcher eventLogDispatcher(IEventSendLogStore sendLogStore, IEventHandleLogStore handleLogStore) {
        return new EventLogDispatcher(sendLogStore, handleLogStore, logProperties);
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

    /**
     * 注册 Eventbus
     */
    @Bean
    public EventBus eventBus(ObjectProvider<IEventSender> eventSender, ObjectProvider<Collection<IEventSendInterceptor>> interceptors) {
        return new EventBus(eventSender.getIfAvailable(), interceptors.getIfAvailable());
    }
}
