package com.bird.service.boot.starter.eventbus.kafka;

import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.handler.EventDispatcher;
import com.bird.eventbus.kafka.handler.EventArgDeserializer;
import com.bird.eventbus.kafka.handler.KafkaEventArgListener;
import com.bird.eventbus.kafka.register.EventArgSerializer;
import com.bird.eventbus.kafka.register.KafkaRegister;
import com.bird.eventbus.register.IEventRegister;
import com.bird.service.boot.starter.eventbus.EventbusConstant;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Configuration
@ConditionalOnProperty(value = EventbusConstant.Kafka.HOST_PROPERTY_NAME)
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfigurer {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME)
    public KafkaTemplate kafkaTemplate() {
        HashMap<String,Object> properties = new HashMap<>(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        KafkaProviderProperties providerProperties = kafkaProperties.getProvider();
        properties.put("retries", providerProperties.getRetries());
        properties.put("batch.size", providerProperties.getBatchSize());
        properties.put("linger.ms", providerProperties.getLingerms());
        properties.put("buffer.memory", providerProperties.getBufferMemory());
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", EventArgSerializer.class);

        DefaultKafkaProducerFactory<String,EventArg> producerFactory = new DefaultKafkaProducerFactory<>(properties);

        KafkaTemplate kafkaTemplate = new KafkaTemplate<>(producerFactory, true);
        kafkaTemplate.setDefaultTopic(providerProperties.getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME)
    public IEventRegister eventRegister(KafkaTemplate kafkaTemplate) {
        KafkaRegister eventRegister = new KafkaRegister();
        eventRegister.setKafkaTemplate(kafkaTemplate);
        return eventRegister;
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.LISTENER_PACKAGES)
    public EventDispatcher eventDispatcher(){
        EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.initWithPackage(kafkaProperties.getListener().getBasePackages());
        return dispatcher;
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.LISTENER_PACKAGES)
    public KafkaMessageListenerContainer kafkaListenerContainer(EventDispatcher eventDispatcher) {

        KafkaEventArgListener listener = new KafkaEventArgListener(eventDispatcher);
        ContainerProperties containerProperties = new ContainerProperties(eventDispatcher.getAllTopics());
        containerProperties.setMessageListener(listener);
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);

        HashMap<String,Object> properties = new HashMap<>(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        KafkaListenerProperties listenerProperties = kafkaProperties.getListener();
        properties.put("group.id", listenerProperties.getGroupId());
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", false);
        properties.put("auto.commit.interval.ms", 1000);
        properties.put("session.timeout.ms", 15000);
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", EventArgDeserializer.class);
        DefaultKafkaConsumerFactory<String,EventArg> consumerFactory = new DefaultKafkaConsumerFactory<>(properties);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
