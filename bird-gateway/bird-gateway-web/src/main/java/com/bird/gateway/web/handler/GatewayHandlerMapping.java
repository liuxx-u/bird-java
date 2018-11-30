package com.bird.gateway.web.handler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.handler.AbstractHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public final class GatewayHandlerMapping extends AbstractHandlerMapping {

    private final GatewayWebHandler webHandler;

    public GatewayHandlerMapping(final GatewayWebHandler webHandler){
        this.webHandler = webHandler;
        setOrder(1);
    }

    @Override
    protected Mono<?> getHandlerInternal(final ServerWebExchange exchange) {
        return Mono.just(webHandler);
    }

    @Override
    protected CorsConfiguration getCorsConfiguration(final Object handler, final ServerWebExchange exchange) {
        return super.getCorsConfiguration(handler, exchange);
    }
}
