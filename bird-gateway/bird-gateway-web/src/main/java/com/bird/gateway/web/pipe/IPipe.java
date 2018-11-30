package com.bird.gateway.web.pipe;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2018/11/29
 */
public interface IPipe {

    /**
     * acquire plugin name.
     * this is plugin name define  if you extend {@linkplain AbstractPipe } you must Provide the right name.
     * if you impl AbstractGatewayPlugin this attribute not use.
     *
     * @return plugin name.
     */
    String named();

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code WebFilter} through the given {@link PipeChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    Mono<Void> execute(ServerWebExchange exchange, PipeChain chain);
}
