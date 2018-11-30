package com.bird.gateway.web.pipe.rpc;

import com.bird.gateway.common.dto.zk.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.common.enums.RpcTypeEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.web.pipe.AbstractPipe;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.pipe.rpc.dubbo.DubboPipe;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/29
 */
public class RpcPipe extends AbstractPipe implements IChainPipe {

    private final DubboPipe dubboPipe;

    public RpcPipe(DubboPipe dubboPipe) {
        this.dubboPipe = dubboPipe;
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition) {

        RpcTypeEnum rpcTypeEnum = RpcTypeEnum.acquireByName(routeDefinition.getRpcType());

        if (Objects.isNull(rpcTypeEnum)) {
            return jsonResult(exchange, JsonResult.error("不支持的rpc类型：" + routeDefinition.getRpcType()));
        }

        if (RpcTypeEnum.DUBBO.getName().equals(rpcTypeEnum.getName())) {
            return dubboPipe.execute(exchange, chain);
        }else {
            return chain.execute(exchange);
        }
    }

    @Override
    public PipeTypeEnum pipeType() {
        return PipeTypeEnum.FUNCTION;
    }

    @Override
    public int getOrder() {
        return PipeEnum.RPC.getCode();
    }

    @Override
    public String named() {
        return PipeEnum.RPC.getName();
    }
}
