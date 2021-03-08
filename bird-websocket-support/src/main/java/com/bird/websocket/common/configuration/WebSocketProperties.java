package com.bird.websocket.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author yuanjian
 */
@Data
@ConfigurationProperties(prefix = "bird.websocket")
public class WebSocketProperties {

    @NestedConfigurationProperty
    private DelayProperties delay = new DelayProperties();
}
