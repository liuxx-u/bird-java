package com.bird.gateway.web.pipe;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public interface PipeChain {

    Mono<Void> execute(ServerWebExchange exchange);
}
