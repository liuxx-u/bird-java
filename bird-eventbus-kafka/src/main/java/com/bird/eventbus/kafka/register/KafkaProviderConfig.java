package com.bird.eventbus.kafka.register;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

/**
 * @author liuxx
 * @date 2018/2/26
 */
@Configuration
public class KafkaProviderConfig {
    @Value("${kafka.bootstrap.server:127.0.0.1}")
    private String host;
    @Value("${kafka.defaultTopic:bird-kafka-default-topic}")
    private String defaultTopic;
    @Value("${kafka.retries:5}")
    private int retries;
    @Value("${kafka.batch.size:16384}")
    private int batchSize;
    @Value("${kafka.linger.ms:3}")
    private int linger;
    @Value("${kafka.buffer.memory:33554432}")
    private long bufferSize;

    @Bean
    public KafkaTemplate kafkaTemplate(){
        HashMap properties = new HashMap();
        properties.put("bootstrap.servers",host);
        properties.put("retries",retries);
        properties.put("batch.size",batchSize);
        properties.put("linger.ms",linger);
        properties.put("buffer.memory",bufferSize);
        properties.put("key.serializer",StringSerializer.class);
        properties.put("value.serializer",EventArgSerializer.class);

        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory(properties);

        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory,true);
        kafkaTemplate.setDefaultTopic(defaultTopic);
        return kafkaTemplate;
    }
}
