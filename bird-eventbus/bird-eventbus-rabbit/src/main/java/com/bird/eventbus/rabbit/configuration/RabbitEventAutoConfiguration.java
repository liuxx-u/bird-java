package com.bird.eventbus.rabbit.configuration;

import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.configuration.EventCoreAutoConfiguration;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.rabbit.consumer.RabbitEventArgListener;
import com.bird.eventbus.rabbit.producer.RabbitEventSender;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.sender.IEventSender;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Configuration
@ConditionalOnClass({ RabbitTemplate.class, Channel.class })
@AutoConfigureAfter({EventCoreAutoConfiguration.class, RabbitAutoConfiguration.class})
public class RabbitEventAutoConfiguration {

    private final Environment environment;
    private final ConfigurableApplicationContext applicationContext;

    public RabbitEventAutoConfiguration(Environment environment, ConfigurableApplicationContext applicationContext) {
        this.environment = environment;
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnBean(RabbitTemplate.class)
    public IEventSender eventSender(RabbitTemplate rabbitTemplate) {
        return new RabbitEventSender(rabbitTemplate);
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Handler.GROUP)
    public Queue eventGroupQueue() {
        String consumerGroup = environment.resolvePlaceholders("${bird.eventbus.handler.group:}");
        return new Queue(consumerGroup, true);
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Handler.GROUP)
    @ConditionalOnBean({IEventRegistry.class, EventMethodInvoker.class})
    public SimpleMessageListenerContainer messageContainer(SimpleRabbitListenerContainerFactory containerFactory, IEventRegistry eventRegistry, EventMethodInvoker eventMethodInvoker) {
        Queue eventQueue = eventGroupQueue();

        for (String topic : eventRegistry.getAllTopics()) {
            this.initExchange(topic, eventQueue);
        }
        RabbitEventArgListener listener = new RabbitEventArgListener(eventMethodInvoker);

        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setQueues(eventQueue);
        endpoint.setMessageListener(listener);

        SimpleMessageListenerContainer container = containerFactory.createListenerContainer(endpoint);
        if(endpoint.getMessageConverter() != null){
            listener.setMessageConverter(endpoint.getMessageConverter());
        }
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
        bindingBeanBuilder.addConstructorArgValue(queue.getName())
                .addConstructorArgValue(Binding.DestinationType.QUEUE)
                .addConstructorArgValue(topic)
                .addConstructorArgValue("")
                .addConstructorArgValue(new HashMap<>(4));
        beanFactory.registerBeanDefinition("rabbit.binding#" + topic, bindingBeanBuilder.getRawBeanDefinition());
    }
}
