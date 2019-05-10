package com.bird.gateway.registry.zookeeper.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/5/10
 */
@Getter
@Setter
public class ZkRegistryProperties {
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
