package com.bird.gateway.web.pipe.rpc.dubbo;

import com.bird.core.exception.UserFriendlyException;
import com.bird.gateway.common.GatewayConstant;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.web.pipe.PipeChain;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

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
        if(result != null){
            exchange.getAttributes().put(GatewayConstant.DUBBO_RPC_RESULT, result);
        }
        exchange.getAttributes().put(GatewayConstant.RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
        return chain.execute(exchange);
    }

    @Override
    protected Observable<Void> resumeWithFallback() {
        return RxReactiveStreams.toObservable(doFallback());
    }

    private Mono<Void> doFallback() {
        String msg = GatewayConstant.ERROR_RESULT;
        if (isFailedExecution() && getExecutionException() != null) {
            String exception = getExecutionException().toString();
            if(exception.startsWith(UserFriendlyException.class.getName())){
                msg = getExecutionException().getMessage();
            }else {
                log.error(exception);
            }
        }

        exchange.getAttributes().put(GatewayConstant.DUBBO_ERROR_MESSAGE, msg);
        return chain.execute(exchange);
    }
}
