package com.bird.gateway.web.pipe.before;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.common.enums.RpcTypeEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.common.route.IRouteDiscovery;
import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.request.RequestDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 网关前置管道
 *
 * 在所有管道执行前执行，从RequestDTO信息中解析RouteDefinition
 *
 *
 * @author liuxx
 * @date 2018/11/28
 */
public class PrePipe implements IChainPipe {

    @Autowired
    private IRouteDiscovery routeDiscovery;

    @Override
    public String named() {
        return PipeEnum.PRE.getName();
    }

    @Override
    public int getOrder() {
        return PipeEnum.PRE.getCode();
    }

    /**
     * 从RequestDTO信息中解析RouteDefinition信息
     *
     * 如果rpc方式为dubbo，从Zookeeper中获取RouteDefinition
     * 如果rpc方式为springCloud，直接将RequestDTO解析为RouteDefinition
     *
     * @param exchange exchange
     * @param chain    chain
     * @return {@code Mono<Void>}
     */
    @Override
    public Mono<Void> execute(final ServerWebExchange exchange, final PipeChain chain) {
        RequestDTO request = exchange.getAttribute(Constants.REQUESTDTO);
        if (Objects.isNull(request) || StringUtils.isBlank(request.getPath())) {
            return jsonResult(exchange, JsonResult.error("请求路径不存在."));
        }
        RouteDefinition routeDefinition = routeDiscovery.get(request.getPath());
        if(routeDefinition == null){
            routeDefinition = new RouteDefinition();
            routeDefinition.setRpcType(RpcTypeEnum.SPRING_CLOUD.getName());
            routeDefinition.setModule(request.getModule());
            routeDefinition.setPath(request.getSubUrl());
            routeDefinition.setEnabled(true);
        }

        if (BooleanUtils.isNotTrue(routeDefinition.getEnabled())) {
            return jsonResult(exchange, JsonResult.error("api不存在或已被禁用."));
        }

        exchange.getAttributes().put(Constants.ROUTE_DEFINITION, routeDefinition);
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

    /**
     * return json result
     * @param exchange the current server exchange
     * @param result json result
     * @return {@code Mono<Void>}
     */
    private Mono<Void> jsonResult(ServerWebExchange exchange ,JsonResult result){
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

        return response.writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(Objects.requireNonNull(JSON.toJSONString(result)).getBytes(Charset.forName("utf8")))));
    }
}

