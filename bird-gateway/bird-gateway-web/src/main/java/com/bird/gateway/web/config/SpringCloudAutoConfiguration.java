package com.bird.gateway.web.config;

import com.bird.gateway.web.pipe.rpc.cloud.SpringCloudPipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * @author liuxx
 * @date 2019/1/23
 */
@Configuration
@ConditionalOnProperty(prefix = "eureka.client.serviceUrl", name = "defaultZone")
@ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class})
@AutoConfigureAfter(RibbonAutoConfiguration.class)
public class SpringCloudAutoConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    /**
     * Instantiates a new Spring cloud auto configuration.
     *
     * @param loadBalancerClient    the load balancer client
     */
    @Autowired(required = false)
    public SpringCloudAutoConfiguration(final LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    /**
     * init springCloud pipe.
     *
     * @return {@linkplain SpringCloudPipe}
     */
    @Bean
    public SpringCloudPipe springCloudPipe() {
        return new SpringCloudPipe(loadBalancerClient);
    }
}
