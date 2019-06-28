package com.bird.gateway.web.pipe.rpc.cloud;

import com.bird.gateway.common.GatewayConstant;
import com.bird.gateway.common.dto.convert.HttpHandle;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.common.RouteDefinition;
import com.bird.gateway.web.pipe.AbstractPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.pipe.rpc.http.HttpCommand;
import com.bird.gateway.web.utils.HystrixBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import rx.Subscription;

import java.net.URI;
import java.util.Objects;

/**
 * @author liuxx
 * @date 2019/1/23
 */
@Slf4j
public class SpringCloudPipe extends AbstractPipe {

    private final LoadBalancerClient loadBalancer;

    public SpringCloudPipe(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition) {
        HttpHandle httpHandle = this.buildHttpHandle(routeDefinition);
        if (httpHandle == null) {
            log.error("discovery server never register this route,module = " + routeDefinition.getModule() + ";path = " + routeDefinition.getPath());
            return Mono.empty();
        }

        HttpCommand command = new HttpCommand(HystrixBuilder.build(httpHandle), exchange, chain, httpHandle);

        return Mono.create((MonoSink<Object> s) -> {
            Subscription sub = command.toObservable().subscribe(s::success, s::error, s::success);
            s.onCancel(sub::unsubscribe);
            if (command.isCircuitBreakerOpen()) {
                log.error(httpHandle.getGroupKey() + ":spring cloud execute circuitBreaker is Open !");
            }
        }).doOnError(throwable -> {
            throwable.printStackTrace();
            exchange.getAttributes().put(GatewayConstant.RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
            chain.execute(exchange);
        }).then();
    }

    private HttpHandle buildHttpHandle(RouteDefinition routeDefinition) {
        if(loadBalancer == null){
            return null;
        }

        String serviceId = routeDefinition.getModule();
        final ServiceInstance serviceInstance = loadBalancer.choose(serviceId);
        if (Objects.isNull(serviceInstance)) {
            return null;
        }

        final URI uri = loadBalancer.reconstructURI(serviceInstance, URI.create(routeDefinition.getPath()));
        HttpHandle httpHandle = new HttpHandle();
        httpHandle.setGroupKey(routeDefinition.getModule());
        httpHandle.setCommandKey(routeDefinition.getPath());
        httpHandle.setUrl(uri.toString());
        httpHandle.setTimeout(180000);
        return httpHandle;
    }

    @Override
    public String named() {
        return PipeEnum.SPRING_CLOUD.getName();
    }
}
