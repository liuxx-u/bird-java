package com.bird.service.boot.starter.eventbus.kafka;

import com.bird.service.boot.starter.eventbus.EventbusConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Getter
@Setter
@ConfigurationProperties(prefix = EventbusConstant.Kafka.PREFIX)
public class KafkaProperties {

    private String host;

    @NestedConfigurationProperty
    private KafkaProviderProperties provider;

    @NestedConfigurationProperty
    private KafkaListenerProperties listener;
}
