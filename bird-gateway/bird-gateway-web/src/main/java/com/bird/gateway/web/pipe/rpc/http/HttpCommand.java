package com.bird.gateway.web.pipe.rpc.http;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.dto.convert.HttpHandle;
import com.bird.gateway.common.enums.HttpMethodEnum;
import com.bird.gateway.common.enums.ResultEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.request.RequestDTO;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liuxx
 * @date 2019/1/22
 */
@Slf4j
public class HttpCommand extends HystrixObservableCommand<Void> {

    private static final WebClient WEB_CLIENT = WebClient.create();

    private final ServerWebExchange exchange;

    private final PipeChain chain;

    private final HttpHandle httpHandle;

    private final RequestDTO request;

    public HttpCommand(Setter setter, ServerWebExchange exchange, PipeChain chain, HttpHandle httpHandle) {
        super(setter);

        this.exchange = exchange;
        this.chain = chain;
        this.httpHandle = httpHandle;
        request = exchange.getAttribute(Constants.REQUESTDTO);
    }

    @Override
    protected Observable<Void> construct() {
        return RxReactiveStreams.toObservable(doHttpInvoke());
    }

    private Mono<Void> doHttpInvoke() {
        String url = httpHandle.getUrl();
        String method = request.getHttpMethod();
        Integer timeout = httpHandle.getTimeout();

        if (method.equalsIgnoreCase(HttpMethodEnum.GET.getName())) {
            return WEB_CLIENT.get().uri(url)
                    .headers(headers -> {
                        headers.addAll(exchange.getRequest().getHeaders());
                        headers.remove(HttpHeaders.HOST);
                    })
                    .exchange()
                    .doOnError(e -> log.error(e.getMessage()))
                    .timeout(Duration.ofMillis(timeout))
                    .flatMap(this::doNext);
        } else if (method.equalsIgnoreCase(HttpMethodEnum.POST.getName())) {
            String mediaType = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE))
                    .orElse(MediaType.APPLICATION_JSON_UTF8_VALUE);

            return WEB_CLIENT.post().uri(url)
                    .headers(httpHeaders -> {
                        httpHeaders.addAll(exchange.getRequest().getHeaders());
                        httpHeaders.remove(HttpHeaders.HOST);
                    })
                    .contentType(MediaType.valueOf(mediaType))
                    .body(BodyInserters.fromDataBuffers(exchange.getRequest().getBody()))
                    .exchange()
                    .doOnError(e -> log.error(e.getMessage()))
                    .timeout(Duration.ofMillis(timeout))
                    .flatMap(this::doNext);
        }
        return Mono.empty();
    }

    private Mono<Void> doNext(final ClientResponse res) {
        if (res.statusCode().is2xxSuccessful()) {
            exchange.getAttributes().put(Constants.RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
        } else {
            exchange.getAttributes().put(Constants.RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
        }
        exchange.getAttributes().put(Constants.HTTP_RPC_RESULT, res);
        return chain.execute(exchange);
    }

    @Override
    protected Observable<Void> resumeWithFallback() {
        return RxReactiveStreams.toObservable(doFallback());
    }

    private Mono<Void> doFallback() {
        final Throwable exception = getExecutionException();
        if (isFailedExecution()) {
            log.error("http execute have error:", exception.getMessage());
        }
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        final JsonResult error = JsonResult.error(exception.getMessage());
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(Objects.requireNonNull(JSON.toJSONString(error)).getBytes(Charset.forName("utf8")))));
    }
}
