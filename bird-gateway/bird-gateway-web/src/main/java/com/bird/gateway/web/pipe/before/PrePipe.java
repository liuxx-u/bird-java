package com.bird.gateway.web.pipe.before;

import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.pipe.IChainPipe;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public class PrePipe implements IChainPipe {

    @Override
    public String named() {
        return PipeEnum.PRE.getName();
    }

    @Override
    public int getOrder() {
        return PipeEnum.PRE.getCode();
    }

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code WebFilter} through the given {@link PipeChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> execute(final ServerWebExchange exchange, final PipeChain chain) {
        return chain.execute(exchange);
    }

    /**
     * return plugin type.
     *
     * @return {@linkplain PipeTypeEnum}
     */
    @Override
    public PipeTypeEnum pipeType() {
        return PipeTypeEnum.BEFORE;
    }
}

