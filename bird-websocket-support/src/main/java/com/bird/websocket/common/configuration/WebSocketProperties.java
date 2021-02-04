package com.bird.websocket.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuanjian
 */
@Data
@ConfigurationProperties(prefix = "bird.websocket")
public class WebSocketProperties {
}
