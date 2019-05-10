package com.bird.gateway.registry.zookeeper.configuration;

import com.bird.gateway.common.IRouteDiscovery;
import com.bird.gateway.registry.zookeeper.discovery.ZkRouteDataListener;
import com.bird.gateway.registry.zookeeper.discovery.ZookeeperCacheManager;
import com.bird.gateway.registry.zookeeper.discovery.ZookeeperRouteDiscovery;
import com.bird.gateway.registry.zookeeper.serializer.ZkSerializerFactory;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2019/5/10
 */
@Configuration
@ConditionalOnProperty(value = "bird.gateway.discovery.zookeeper.url")
public class ZkDiscoveryAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "bird.gateway.discovery.zookeeper")
    public ZkRegistryProperties registryProperties() {
        return new ZkRegistryProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ZkClient.class)
    public ZkClient zkClient(ZkRegistryProperties properties) {
        return new ZkClient(properties.getUrl(), properties.getSessionTimeout(), properties.getConnectionTimeout(), ZkSerializerFactory.of(properties.getSerializer()));
    }

    @Bean
    public ZkRouteDataListener zkRouteDataListener(){
        return new ZkRouteDataListener();
    }

    @Bean
    public ZookeeperCacheManager zookeeperCacheManager(ZkClient zkClient, ZkRouteDataListener zkRouteDataListener){
        return new ZookeeperCacheManager(zkClient,zkRouteDataListener);
    }

    @Bean
    public IRouteDiscovery routeDiscovery(){
        return new ZookeeperRouteDiscovery();
    }
}
