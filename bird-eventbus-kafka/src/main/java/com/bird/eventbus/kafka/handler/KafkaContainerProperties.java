package com.bird.eventbus.kafka.handler;

import com.bird.eventbus.handler.DefaultEventHandlerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;

/**
 * 继承ContainerProperties，实现系统Topics的自动查找
 */
public class KafkaContainerProperties extends ContainerProperties {
    public KafkaContainerProperties() {
        super(DefaultEventHandlerFactory.getAllTopics());
    }
}
