package com.bird.gateway.web.pipe.before;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.request.RequestDTO;
import com.bird.gateway.web.zookeeper.ZookeeperCacheManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
        RequestDTO request = exchange.getAttribute(Constants.REQUESTDTO);
        if (Objects.isNull(request) || StringUtils.isBlank(request.getPath())) {
            return jsonResult(exchange, JsonResult.error("请求路径不存在."));
        }
        RouteDefinition routeDefinition = ZookeeperCacheManager.getRouteDefinition(request.getPath());
        if (routeDefinition == null || BooleanUtils.isNotTrue(routeDefinition.getEnabled())) {
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
                .wrap(Objects.requireNonNull(JSON.toJSONString(result)).getBytes())));
    }
}

