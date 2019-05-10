package com.bird.gateway.web.pipe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.bird.gateway.common.GatewayConstant;
import com.bird.gateway.common.dto.JsonResult;
import com.bird.gateway.common.RouteDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Slf4j
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public abstract class AbstractPipe implements IPipe {

    private static SimplePropertyPreFilter JSON_FILTER = new SimplePropertyPreFilter();
    static {
        JSON_FILTER.getExcludes().add("class");
    }

    /**
     * this is Template Method child has Implement your own logic.
     *
     * @param exchange exchange the current server exchange {@linkplain ServerWebExchange}
     * @param chain    chain the current chain  {@linkplain ServerWebExchange}
     * @param routeDefinition     rule    {@linkplain RouteDefinition}
     * @return {@code Mono<Void>} to indicate when request handling is complete
     */
    protected abstract Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition);

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code WebFilter} through the given {@link PipeChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PipeChain chain) {
        RouteDefinition routeDefinition = exchange.getAttribute(GatewayConstant.ROUTE_DEFINITION);
        if (routeDefinition == null || BooleanUtils.isNotTrue(routeDefinition.getEnabled())) {
            return jsonResult(exchange, JsonResult.error("api不存在或已被禁用."));
        }

        return doExecute(exchange,chain,routeDefinition);
    }

    /**
     * return json result
     * @param exchange the current server exchange
     * @param result json result
     * @return {@code Mono<Void>}
     */
    protected Mono<Void> jsonResult(ServerWebExchange exchange ,JsonResult result){
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

        return response.writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(Objects.requireNonNull(JSON.toJSONString(result,JSON_FILTER
                                , SerializerFeature.PrettyFormat
                                , SerializerFeature.QuoteFieldNames
                                , SerializerFeature.WriteDateUseDateFormat
                                , SerializerFeature.WriteNullStringAsEmpty
                                , SerializerFeature.WriteNullNumberAsZero
                                , SerializerFeature.WriteNullBooleanAsFalse)).getBytes(Charset.forName("utf8")))));
    }
}
