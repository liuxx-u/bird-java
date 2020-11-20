package com.bird.eventbus.kafka.configuration;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.configuration.EventCoreAutoConfiguration;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.kafka.consumer.KafkaEventArgListener;
import com.bird.eventbus.kafka.consumer.KafkaEventConsumerFactoryCustomizer;
import com.bird.eventbus.kafka.producer.KafkaEventProducerFactoryCustomizer;
import com.bird.eventbus.kafka.producer.KafkaEventSender;
import com.bird.eventbus.log.IEventLogDispatcher;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.sender.IEventSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KafkaTemplate.class)
@AutoConfigureAfter({EventCoreAutoConfiguration.class, KafkaAutoConfiguration.class})
public class EventKafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DefaultKafkaProducerFactoryCustomizer.class)
    @ConditionalOnProperty(value = "spring.kafka.producer.bootstrap-servers")
    public DefaultKafkaProducerFactoryCustomizer kafkaProducerFactoryCustomizer(){
        return new KafkaEventProducerFactoryCustomizer();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.producer.bootstrap-servers")
    public IEventSender eventSender(ObjectProvider<IEventLogDispatcher> eventLogDispatcher, KafkaTemplate<String, IEventArg> kafkaTemplate) {
        return new KafkaEventSender(eventLogDispatcher.getIfAvailable(), kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(DefaultKafkaConsumerFactoryCustomizer.class)
    @ConditionalOnProperty(value = "spring.kafka.consumer.bootstrap-servers")
    public DefaultKafkaConsumerFactoryCustomizer kafkaConsumerFactoryCustomizer(IEventRegistry eventRegistry){
        return new KafkaEventConsumerFactoryCustomizer(eventRegistry);
    }

    @Bean
    @ConditionalOnBean({EventMethodInvoker.class, IEventRegistry.class})
    @ConditionalOnProperty(value = "spring.kafka.consumer.bootstrap-servers")
    public KafkaMessageListenerContainer kafkaListenerContainer(ConsumerFactory<String,IEventArg> consumerFactory, EventMethodInvoker eventMethodInvoker, IEventRegistry eventRegistry) {

        KafkaEventArgListener listener = new KafkaEventArgListener(eventMethodInvoker);
        ContainerProperties containerProperties = new ContainerProperties(eventRegistry.getAllTopics());
        containerProperties.setMessageListener(listener);
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
