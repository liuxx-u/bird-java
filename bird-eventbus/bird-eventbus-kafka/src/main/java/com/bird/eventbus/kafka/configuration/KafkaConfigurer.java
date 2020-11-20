package com.bird.eventbus.kafka.configuration;

import com.bird.eventbus.EventbusConstant;
import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.configuration.EventCoreAutoConfiguration;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.kafka.handler.EventArgDeserializer;
import com.bird.eventbus.kafka.handler.KafkaEventArgListener;
import com.bird.eventbus.kafka.register.EventArgSerializer;
import com.bird.eventbus.kafka.register.KafkaEventSender;
import com.bird.eventbus.log.IEventLogDispatcher;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.sender.IEventSender;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Configuration
@ConditionalOnProperty(value = EventbusConstant.Kafka.HOST_PROPERTY_NAME)
@EnableConfigurationProperties(KafkaProperties.class)
@AutoConfigureAfter(EventCoreAutoConfiguration.class)
public class KafkaConfigurer {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME)
    public KafkaTemplate<String, IEventArg> kafkaTemplate() {
        HashMap<String, Object> properties = new HashMap<>(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        KafkaProviderProperties providerProperties = kafkaProperties.getProvider();
        properties.put("retries", providerProperties.getRetries());
        properties.put("batch.size", providerProperties.getBatchSize());
        properties.put("linger.ms", providerProperties.getLingerms());
        properties.put("buffer.memory", providerProperties.getBufferMemory());
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", EventArgSerializer.class);

        DefaultKafkaProducerFactory<String, IEventArg> producerFactory = new DefaultKafkaProducerFactory<>(properties);

        KafkaTemplate<String, IEventArg> kafkaTemplate = new KafkaTemplate<>(producerFactory, true);
        kafkaTemplate.setDefaultTopic(providerProperties.getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    @ConditionalOnProperty(value = EventbusConstant.Kafka.PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME)
    public IEventSender eventSender(ObjectProvider<IEventLogDispatcher> eventLogDispatcher, KafkaTemplate<String, IEventArg> kafkaTemplate) {
        return new KafkaEventSender(eventLogDispatcher.getIfAvailable(), kafkaTemplate);
    }

    @Bean
    @ConditionalOnBean({EventMethodInvoker.class, IEventRegistry.class})
    public KafkaMessageListenerContainer kafkaListenerContainer(EventMethodInvoker eventMethodInvoker, IEventRegistry eventRegistry) {

        KafkaEventArgListener listener = new KafkaEventArgListener(eventMethodInvoker);
        ContainerProperties containerProperties = new ContainerProperties(eventRegistry.getAllTopics());
        containerProperties.setMessageListener(listener);
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        HashMap<String, Object> properties = new HashMap<>(8);
        properties.put("bootstrap.servers", kafkaProperties.getHost());

        KafkaListenerProperties listenerProperties = kafkaProperties.getListener();
        properties.put("group.id", listenerProperties.getGroupId());
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", false);
        properties.put("auto.commit.interval.ms", 1000);
        properties.put("session.timeout.ms", 15000);
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", EventArgDeserializer.class);
        DefaultKafkaConsumerFactory<String, EventArg> consumerFactory = new DefaultKafkaConsumerFactory<>(properties);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
