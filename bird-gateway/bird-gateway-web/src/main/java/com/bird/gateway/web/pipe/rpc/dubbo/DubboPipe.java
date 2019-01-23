package com.bird.gateway.web.pipe.rpc.dubbo;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.web.pipe.AbstractPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.utils.HystrixBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import rx.Subscription;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Slf4j
public class DubboPipe extends AbstractPipe {

    private final DubboProxyService dubboProxyService;

    public DubboPipe(DubboProxyService dubboProxyService) {
        this.dubboProxyService = dubboProxyService;
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition) {

        DubboHandle dubboHandle = JSON.parseObject(routeDefinition.getRpcJson(), DubboHandle.class);

        if (!checkData(dubboHandle)) {
            return chain.execute(exchange);
        }

        DubboCommand command = new DubboCommand(HystrixBuilder.build(dubboHandle), exchange, chain, dubboHandle, dubboProxyService);
        return Mono.create((MonoSink<Object> s) -> {
            Subscription sub = command.toObservable().subscribe(s::success,
                    s::error, s::success);
            s.onCancel(sub::unsubscribe);
            if (command.isCircuitBreakerOpen()) {
                log.error(dubboHandle.getGroupKey() + ":dubbo execute circuitBreaker is Open !");
            }
        }).doOnError(throwable -> {
            throwable.printStackTrace();
            exchange.getAttributes().put(Constants.RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
            chain.execute(exchange);
        }).then();
    }

    private boolean checkData(final DubboHandle dubboHandle) {
        if (Objects.isNull(dubboHandle)
                || StringUtils.isBlank(dubboHandle.getRegistry())
                || StringUtils.isBlank(dubboHandle.getAppName())
                || StringUtils.isBlank(dubboHandle.getInterfaceName())
                || StringUtils.isBlank(dubboHandle.getMethodName())) {
            log.error("dubbo handle require param not config!");
            return false;
        }

        if (StringUtils.isBlank(dubboHandle.getGroupKey())) {
            dubboHandle.setGroupKey(dubboHandle.getInterfaceName());
        }
        if (StringUtils.isBlank(dubboHandle.getCommandKey())) {
            dubboHandle.setCommandKey(dubboHandle.getMethodName());
        }
        return true;
    }

    @Override
    public String named() {
        return PipeEnum.DUBBO.getName();
    }
}
