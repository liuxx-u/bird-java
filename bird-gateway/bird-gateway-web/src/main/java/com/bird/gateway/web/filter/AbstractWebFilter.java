package com.bird.gateway.web.filter;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public abstract class AbstractWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        return doFilter(exchange, chain).switchIfEmpty(Mono.just(false))
                .flatMap(filterResult -> filterResult ? chain.filter(exchange) : doDenyResponse(exchange));
    }

    /**
     * this is Template Method ,children Implement your own filtering logic.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Boolean>} result：TRUE (is pass)，and flow next filter；FALSE (is not pass) execute doDenyResponse(ServerWebExchange exchange)
     */
    protected abstract Mono<Boolean> doFilter(ServerWebExchange exchange, WebFilterChain chain);

    /**
     * this is Template Method ,children Implement your own And response client.
     *
     * @param exchange the current server exchange.
     * @return {@code Mono<Void>} response msg.
     */
    protected abstract Mono<Void> doDenyResponse(ServerWebExchange exchange);

}
