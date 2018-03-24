package com.bird.service.boot.starter.eventbus.kafka;

import com.bird.service.boot.starter.eventbus.EventbusConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@ConfigurationProperties(prefix = EventbusConstant.KAFKA.PREFIX)
public class KafkaProperties {

    private String host;

    @NestedConfigurationProperty
    private KafkaProviderProperties provider;

    @NestedConfigurationProperty
    private KafkaListenerProperties listener;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public KafkaProviderProperties getProvider() {
        return provider;
    }

    public void setProvider(KafkaProviderProperties provider) {
        this.provider = provider;
    }

    public KafkaListenerProperties getListener() {
        return listener;
    }

    public void setListener(KafkaListenerProperties listener) {
        this.listener = listener;
    }
}
