package com.bird.gateway.web.config;

import com.bird.gateway.configuration.zookeeper.ZookeeperConfigurer;
import com.bird.gateway.web.filter.ParamWebFilter;
import com.bird.gateway.web.handler.GatewayHandlerMapping;
import com.bird.gateway.web.handler.GatewayWebHandler;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.after.ResultPipe;
import com.bird.gateway.web.pipe.before.AuthorizePipe;
import com.bird.gateway.web.pipe.before.PrePipe;
import com.bird.gateway.web.pipe.rpc.RpcPipe;
import com.bird.gateway.web.pipe.rpc.dubbo.DubboPipe;
import com.bird.gateway.web.pipe.rpc.dubbo.DubboProxyService;
import com.bird.gateway.web.zookeeper.ZkRouteDataListener;
import com.bird.gateway.web.zookeeper.ZookeeperCacheManager;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Configuration
@Import(ZookeeperConfigurer.class)
public class GatewayWebConfigurer {

    /**
     * register ZkRouteDataListener in spring ioc.
     *
     * @return ZkRouteDataListener {@linkplain ZkRouteDataListener}
     */
    @Bean
    public ZkRouteDataListener zkRouteDataListener(){
        return new ZkRouteDataListener();
    }

    /**
     * register ZookeeperCacheManager in spring ioc.
     *
     * @return ZookeeperCacheManager {@linkplain ZookeeperCacheManager}
     */
    @Bean
    public ZookeeperCacheManager zookeeperCacheManager(ZkClient zkClient,ZkRouteDataListener zkRouteDataListener){
        return new ZookeeperCacheManager(zkClient,zkRouteDataListener);
    }

    @Bean
    public DubboProxyService dubboProxyService(){
        return new DubboProxyService();
    }

    /**
     * init dubbo pipe.
     *
     * @return {@linkplain DubboPipe}
     */
    @Bean
    public DubboPipe dubboPipe(DubboProxyService dubboProxyService){
        return new DubboPipe(dubboProxyService);
    }

    /**
     * init pre pipe.
     *
     * @return {@linkplain PrePipe}
     */
    @Bean
    public IChainPipe prePipe() {
        return new PrePipe();
    }

    /**
     * init authorize pipe.
     *
     * @return {@linkplain AuthorizePipe}
     */
    @Bean
    public IChainPipe authorizePipe(){
        return new AuthorizePipe();
    }

    /**
     * init rpc pipe.
     *
     * @return {@linkplain RpcPipe}
     */
    @Bean
    public IChainPipe rpcPipe(DubboPipe dubboPipe){
        return new RpcPipe(dubboPipe);
    }

    /**
     * init result pipe.
     *
     * @return {@linkplain RpcPipe}
     */
    @Bean
    public IChainPipe resultPipe(){
        return new ResultPipe();
    }

    /**
     * init gatewayWebHandler.
     *
     * @param pipes this pipes is All impl IChainPipe.
     * @return {@linkplain GatewayWebHandler}
     */
    @Bean
    public GatewayWebHandler gatewayWebHandler(final List<IChainPipe> pipes) {

        final List<IChainPipe> sortedPipes = pipes.stream()
                .sorted((m, n) -> {
                    if (m.pipeType().equals(n.pipeType())) {
                        return m.getOrder() - n.getOrder();
                    } else {
                        return m.pipeType().getName().compareTo(n.pipeType().getName());
                    }
                }).collect(Collectors.toList());
        return new GatewayWebHandler(sortedPipes);
    }

    /**
     * init  GatewayHandlerMapping.
     *
     * @param webHandler {@linkplain GatewayWebHandler}
     * @return {@linkplain GatewayHandlerMapping}
     */
    @Bean
    public GatewayHandlerMapping gatewayHandlerMapping(final GatewayWebHandler webHandler) {
        return new GatewayHandlerMapping(webHandler);
    }

    /**
     * init param web filter.
     *
     * @return {@linkplain ParamWebFilter}
     */
    @Bean
    @Order(1)
    public WebFilter paramWebFilter() {
        return new ParamWebFilter();
    }
}
