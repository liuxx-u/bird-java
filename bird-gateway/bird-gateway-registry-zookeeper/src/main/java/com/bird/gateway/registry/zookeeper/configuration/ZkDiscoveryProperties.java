package com.bird.gateway.registry.zookeeper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2019/5/24
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bird.gateway.discovery.zookeeper")
public class ZkDiscoveryProperties {
    /**
     * zookeeper url
     */
    private String url;

    /**
     * zookeeper session time out
     */
    private Integer sessionTimeout = 5000;

    /**
     * zookeeper connection time out
     */
    private Integer connectionTimeout = 2000;

    /**
     * zookeeper serializer
     */
    private String serializer;
}
