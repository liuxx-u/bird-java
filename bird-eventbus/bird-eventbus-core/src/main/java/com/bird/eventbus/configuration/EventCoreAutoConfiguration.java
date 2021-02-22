package com.bird.eventbus.configuration;

import com.bird.eventbus.EventBus;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.log.*;
import com.bird.eventbus.sender.IEventSendInterceptor;
import com.bird.eventbus.sender.IEventSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(EventLogProperties.class)
public class EventCoreAutoConfiguration {

    private final EventLogProperties logProperties;

    public EventCoreAutoConfiguration(EventLogProperties logProperties) {
        this.logProperties = logProperties;
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
        EventLogDispatcher eventLogDispatcher = new EventLogDispatcher(sendLogStore, handleLogStore, logProperties);
        eventLogDispatcher.init();
        return eventLogDispatcher;
    }

    /**
     * 注册 Eventbus
     */
    @Bean
    public EventBus eventBus(ObjectProvider<IEventSender> eventSender, ObjectProvider<EventMethodInvoker> eventMethodInvoker, ObjectProvider<Collection<IEventSendInterceptor>> interceptors) {
        return new EventBus(eventSender.getIfAvailable(), eventMethodInvoker.getIfAvailable(), interceptors.getIfAvailable());
    }
}
