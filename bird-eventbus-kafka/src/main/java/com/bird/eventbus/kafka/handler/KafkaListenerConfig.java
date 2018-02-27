package com.bird.eventbus.kafka.handler;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2018/2/26
 */

@Configuration
public class KafkaListenerConfig {

    @Value("${kafka.bootstrap.server:127.0.0.1}")
    private String host;
    @Value("${kafka.group.id:bird-kafka}")
    private String groupId;

    @Bean
    public KafkaEventArgListener kafkaEventArgListener(){
        return new KafkaEventArgListener();
    }

    @Bean
    public KafkaContainerProperties kafkaContainerProperties(KafkaEventArgListener kafkaEventArgListener){
        KafkaContainerProperties containerProperties = new KafkaContainerProperties();
        containerProperties.setMessageListener(kafkaEventArgListener);
        return containerProperties;
    }

    @Bean
    public KafkaMessageListenerContainer kafkaListenerContainer(KafkaContainerProperties containerProperties) {
        HashMap properties = new HashMap();
        properties.put("bootstrap.servers", host);
        properties.put("group.id", groupId);
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", true);
        properties.put("auto.commit.interval.ms", 1000);
        properties.put("session.timeout.ms", 15000);
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", EventArgDeserializer.class);

        DefaultKafkaConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory(properties);

        return new KafkaMessageListenerContainer(consumerFactory, containerProperties);
    }
}
