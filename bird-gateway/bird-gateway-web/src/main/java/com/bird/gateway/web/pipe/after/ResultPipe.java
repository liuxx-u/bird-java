package com.bird.gateway.web.pipe.after;

import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.enums.RpcTypeEnum;
import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.common.exception.GatewayException;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.web.pipe.AbstractPipe;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.PipeChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/30
 */
@Slf4j
@SuppressWarnings("all")
public class ResultPipe extends AbstractPipe implements IChainPipe {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition) {
        return chain.execute(exchange).then(Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            final String resultType = exchange.getAttribute(Constants.RESPONSE_RESULT_TYPE);
            if(StringUtils.equalsIgnoreCase(resultType,ResultEnum.ERROR.getName())){

            }else if(StringUtils.equalsIgnoreCase(resultType,ResultEnum.TIME_OUT.getName())){

            }else {
                RpcTypeEnum rpcTypeEnum = RpcTypeEnum.acquireByName(routeDefinition.getRpcType());
                if (RpcTypeEnum.DUBBO.getName().equals(rpcTypeEnum.getName())) {
                    try {
                        final Object result = exchange.getAttribute(Constants.DUBBO_RPC_RESULT);
                        return jsonResult(exchange,JsonResult.success(result));
                    }catch (GatewayException e){
                        return jsonResult(exchange,JsonResult.success(null));
                    }
                }else if(RpcTypeEnum.SPRING_CLOUD.getName().equals(rpcTypeEnum.getName())){
                    ClientResponse clientResponse = exchange.getAttribute(Constants.HTTP_RPC_RESULT);
                    if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR || Objects.isNull(clientResponse)) {
                        return jsonResult(exchange,JsonResult.error(""));
                    }

                    return response.writeWith(clientResponse.body(BodyExtractors.toDataBuffers()));
                }
            }
            return Mono.empty();
        }));
    }

    @Override
    public int getOrder() {
        return PipeEnum.RESULT.getCode();
    }

    @Override
    public PipeTypeEnum pipeType() {
        return PipeTypeEnum.LAST;
    }

    @Override
    public String named() {
        return PipeEnum.RESULT.getName();
    }
}
