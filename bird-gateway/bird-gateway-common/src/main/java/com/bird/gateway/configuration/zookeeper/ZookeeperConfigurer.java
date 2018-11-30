package com.bird.gateway.configuration.zookeeper;

import com.bird.gateway.configuration.zookeeper.serializer.ZkSerializerFactory;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Configuration
public class ZookeeperConfigurer {

    private final Environment env;

    /**
     * Instantiates a new Zookeeper configuration.
     *
     * @param env the env
     */
    @Autowired
    public ZookeeperConfigurer(final Environment env) {
        this.env = env;
    }

    /**
     * register zkClient in spring ioc.
     *
     * @return ZkClient {@linkplain ZkClient}
     */
    @Bean
    public ZkClient zkClient() {
        return new ZkClient(env.getProperty("spring.zookeeper.url"),
                Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.zookeeper.sessionTimeout"))),
                Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.zookeeper.connectionTimeout"))),
                ZkSerializerFactory.of(env.getProperty("spring.zookeeper.serializer")));
    }
}
