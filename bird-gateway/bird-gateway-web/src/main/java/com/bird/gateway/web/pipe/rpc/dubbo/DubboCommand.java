package com.bird.gateway.web.pipe.rpc.dubbo;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.web.pipe.PipeChain;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Slf4j
public class DubboCommand extends HystrixObservableCommand<Void> {

    private final ServerWebExchange exchange;

    private final PipeChain chain;

    private final DubboHandle dubboHandle;

    private final DubboProxyService dubboProxyService;

    DubboCommand(Setter setter, ServerWebExchange exchange, PipeChain chain, DubboHandle dubboHandle, DubboProxyService dubboProxyService) {
        super(setter);

        this.exchange = exchange;
        this.chain = chain;
        this.dubboHandle = dubboHandle;
        this.dubboProxyService = dubboProxyService;
    }

    @Override
    protected Observable<Void> construct() {
        return RxReactiveStreams.toObservable(doRpcInvoke());
    }

    private Mono<Void> doRpcInvoke() {
        final Object result = dubboProxyService.genericInvoker(exchange, dubboHandle);
        exchange.getAttributes().put(Constants.DUBBO_RPC_RESULT, result);
        exchange.getAttributes().put(Constants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
        return chain.execute(exchange);
    }

    @Override
    protected Observable<Void> resumeWithFallback() {
        return RxReactiveStreams.toObservable(doFallback());
    }

    private Mono<Void> doFallback() {
        if (isFailedExecution()) {
            log.error("dubbo rpc have error:" + getExecutionException().getMessage());
        }
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        final JsonResult error = JsonResult.error(Constants.DUBBO_ERROR_RESULT);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(Objects.requireNonNull(JSON.toJSONString(error)).getBytes())));
    }
}
