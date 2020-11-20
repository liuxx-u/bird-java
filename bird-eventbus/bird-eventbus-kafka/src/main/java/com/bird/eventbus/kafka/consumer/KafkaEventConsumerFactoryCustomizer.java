package com.bird.eventbus.kafka.consumer;

import com.bird.eventbus.registry.IEventRegistry;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

/**
 * @author liuxx
 * @since 2020/11/20
 */
public class KafkaEventConsumerFactoryCustomizer implements DefaultKafkaConsumerFactoryCustomizer {

    private final IEventRegistry eventRegistry;

    public KafkaEventConsumerFactoryCustomizer(IEventRegistry eventRegistry){
        this.eventRegistry = eventRegistry;
    }

    /**
     * Customize the {@link DefaultKafkaConsumerFactory}.
     *
     * @param consumerFactory the consumer factory to customize
     */
    @Override
    public void customize(DefaultKafkaConsumerFactory<?, ?> consumerFactory) {
        consumerFactory.setValueDeserializer(new FastJsonDeserializer<>(this.eventRegistry.getAllTopics()));
    }
}
