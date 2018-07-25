package com.bird.service.boot.starter.eventbus.kafka;

import com.bird.eventbus.handler.EventHandlerFactory;
import com.bird.eventbus.kafka.handler.EventArgDeserializer;
import com.bird.eventbus.kafka.handler.KafkaEventArgListener;
import com.bird.eventbus.kafka.register.EventArgSerializer;
import com.bird.eventbus.kafka.register.KafkaRegister;
import com.bird.eventbus.register.IEventRegister;
import com.bird.service.boot.starter.eventbus.EventbusConstant;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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

import javax.inject.Inject;
import java.util.HashMap;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Configuration
@ConditionalOnProperty(value = EventbusConstant.Kafka.HOST_PROPERTY_NAME)
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfigurer {

    @Inject
    private KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME)
    public KafkaTemplate kafkaTemplate() {
        HashMap properties = new HashMap(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        KafkaProviderProperties providerProperties = kafkaProperties.getProvider();
        properties.put("retries", providerProperties.getRetries());
        properties.put("batch.size", providerProperties.getBatchSize());
        properties.put("linger.ms", providerProperties.getLingerms());
        properties.put("buffer.memory", providerProperties.getBufferMemory());
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", EventArgSerializer.class);

        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory(properties);

        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory, true);
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
    @ConditionalOnProperty(value = EventbusConstant.Kafka.LISTENER_GROUP_ID)
    public KafkaMessageListenerContainer kafkaListenerContainer() {

        KafkaListenerProperties listenerProperties = kafkaProperties.getListener();

        //初始化EventHandlerFactory
        EventHandlerFactory.initWithPackage(listenerProperties.getBasePackages());

        KafkaEventArgListener listener = new KafkaEventArgListener();
        ContainerProperties containerProperties = new ContainerProperties(EventHandlerFactory.getAllTopics());
        containerProperties.setMessageListener(listener);
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);

        HashMap properties = new HashMap(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        properties.put("group.id", listenerProperties.getGroupId());
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", false);
        properties.put("auto.commit.interval.ms", 1000);
        properties.put("session.timeout.ms", 15000);
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", EventArgDeserializer.class);
        DefaultKafkaConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory(properties);

        return new KafkaMessageListenerContainer(consumerFactory, containerProperties);
    }
}
