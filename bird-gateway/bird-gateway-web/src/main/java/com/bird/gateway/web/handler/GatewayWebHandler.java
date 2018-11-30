package com.bird.gateway.web.handler;

import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.pipe.IChainPipe;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public final class GatewayWebHandler implements WebHandler {

    private List<IChainPipe> pipes;

    public GatewayWebHandler(final List<IChainPipe> pipes){
        this.pipes = pipes;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        return new DefaultGatewayPipeChain(pipes).execute(exchange).doOnError(Throwable::printStackTrace);
    }

    private static class DefaultGatewayPipeChain implements PipeChain {

        private int index;

        private final List<IChainPipe> pipes;

        /**
         * Instantiates a new Default gateway pipe chain.
         *
         * @param pipes the pipes
         */
        DefaultGatewayPipeChain(final List<IChainPipe> pipes) {
            this.pipes = pipes;
        }

        /**
         * Delegate to the next {@code WebFilter} in the chain.
         *
         * @param exchange the current server exchange
         * @return {@code Mono<Void>} to indicate when request handling is complete
         */
        @Override
        public Mono<Void> execute(final ServerWebExchange exchange) {
            if (this.index < pipes.size()) {
                IChainPipe pipe = pipes.get(this.index++);
                return pipe.execute(exchange, this);
            } else {
                return Mono.empty();
            }
        }
    }
}
