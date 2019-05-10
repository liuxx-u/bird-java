package com.bird.gateway.registry.zookeeper.configuration;

import com.bird.gateway.common.route.IRouteRegistry;
import com.bird.gateway.common.route.IRouteScanner;
import com.bird.gateway.registry.zookeeper.RouteInitializeApplicationListener;
import com.bird.gateway.registry.zookeeper.ZookeeperRouteRegistry;
import com.bird.gateway.registry.zookeeper.serializer.ZkSerializerFactory;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/10
 */
@Configuration
@ConditionalOnProperty(value = "bird.gateway.registry.zookeeper.url")
public class ZkRegistryAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "bird.gateway.registry.zookeeper")
    public ZkRegistryProperties registryProperties() {
        return new ZkRegistryProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ZkClient.class)
    public ZkClient zkClient(ZkRegistryProperties properties) {
        return new ZkClient(properties.getUrl(), properties.getSessionTimeout(), properties.getConnectionTimeout(), ZkSerializerFactory.of(properties.getSerializer()));
    }

    @Bean
    @ConditionalOnMissingBean(IRouteRegistry.class)
    public IRouteRegistry routeRegistry(ZkClient zkClient){
        return new ZookeeperRouteRegistry(zkClient);
    }

    @Bean
    @ConditionalOnBean(IRouteScanner.class)
    public RouteInitializeApplicationListener routeInitializeApplicationListener(List<IRouteScanner> scanners, IRouteRegistry registry){
        return new RouteInitializeApplicationListener(scanners,registry);
    }
}
