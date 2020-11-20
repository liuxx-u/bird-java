package com.bird.eventbus.kafka.producer;

import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

/**
 * @author liuxx
 * @since 2020/11/20
 */
public class KafkaEventProducerFactoryCustomizer implements DefaultKafkaProducerFactoryCustomizer {
    /**
     * Customize the {@link DefaultKafkaProducerFactory}.
     *
     * @param producerFactory the producer factory to customize
     */
    @Override
    public void customize(DefaultKafkaProducerFactory<?, ?> producerFactory) {
        producerFactory.setValueSerializer(new FastJsonSerializer<>());
    }
}
