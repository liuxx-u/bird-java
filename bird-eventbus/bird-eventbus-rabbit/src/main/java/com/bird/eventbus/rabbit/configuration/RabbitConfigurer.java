package com.bird.eventbus.rabbit.configuration;

import com.bird.eventbus.EventBus;
import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.configuration.EventCoreAutoConfiguration;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.rabbit.handler.RabbitEventArgListener;
import com.bird.eventbus.rabbit.register.RabbitEventSender;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.sender.IEventSender;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Configuration
@ConditionalOnProperty(value = EventbusConstant.Rabbit.ADDRESS_PROPERTY_NAME)
@EnableConfigurationProperties(RabbitProperties.class)
@AutoConfigureAfter(EventCoreAutoConfiguration.class)
public class RabbitConfigurer {

    @Value("${spring.application.name:}")
    private String application;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(EventBus.class)
    public EventBus eventBus(){
        return new EventBus();
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitProperties.getAddress());
        connectionFactory.setUsername(rabbitProperties.getUser());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(messageConverter());

        return template;
    }

    @Bean
    public IEventSender eventSender(RabbitTemplate rabbitTemplate) {
        return new RabbitEventSender(rabbitTemplate);
    }

    @Bean
    @ConditionalOnBean({IEventRegistry.class,EventMethodInvoker.class})
    public Queue queue() {
        return new Queue(application, true);
    }

    @Bean
    @ConditionalOnBean({IEventRegistry.class,EventMethodInvoker.class})
    public SimpleMessageListenerContainer messageContainer(Queue queue, IEventRegistry eventRegistry, EventMethodInvoker eventMethodInvoker) {
        for (String topic : eventRegistry.getAllTopics()) {
            this.initExchange(topic,queue);
        }

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue);
        container.setMessageConverter(messageConverter());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        RabbitEventArgListener listener = new RabbitEventArgListener(eventMethodInvoker);
        container.setMessageListener(listener);
        return container;
    }

    /**
     * 初始化topic的Exchange并绑定到Queue，手动注入到Spring容器
     *
     * @param topic topic
     * @param queue queue
     */
    private void initExchange(String topic, Queue queue) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        BeanDefinitionBuilder exchangeBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class);
        exchangeBeanBuilder.addConstructorArgValue(topic);
        beanFactory.registerBeanDefinition("rabbit.exchange#" + topic, exchangeBeanBuilder.getRawBeanDefinition());

        BeanDefinitionBuilder bindingBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class);
        bindingBeanBuilder.addConstructorArgValue(queue.getName());
        bindingBeanBuilder.addConstructorArgValue(Binding.DestinationType.QUEUE);
        bindingBeanBuilder.addConstructorArgValue(topic);
        bindingBeanBuilder.addConstructorArgValue("");
        bindingBeanBuilder.addConstructorArgValue(new HashMap<>());
        beanFactory.registerBeanDefinition("rabbit.binding#" + topic, bindingBeanBuilder.getRawBeanDefinition());
    }
}
