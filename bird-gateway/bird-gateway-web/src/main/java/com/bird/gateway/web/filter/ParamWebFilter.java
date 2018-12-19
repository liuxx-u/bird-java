package com.bird.gateway.web.filter;

import com.alibaba.fastjson.JSON;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.enums.HttpMethodEnum;
import com.bird.gateway.common.result.JsonResult;
import com.bird.gateway.web.request.RequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public class ParamWebFilter extends AbstractWebFilter {

    @Override
    protected Mono<Boolean> doFilter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final RequestDTO request = RequestDTO.transform(exchange.getRequest());
        if (this.verify(request)) {
            exchange.getAttributes().put(Constants.REQUESTDTO, request);
        } else {
            return Mono.just(false);
        }
        return Mono.just(true);
    }

    @Override
    protected Mono<Void> doDenyResponse(final ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        final JsonResult result = JsonResult.error("api不存在或请求方式有误.");
        return response.writeWith(Mono.just(response.bufferFactory()
                .wrap(JSON.toJSONString(result).getBytes())));
    }

    private Boolean verify(final RequestDTO requestDTO) {
        return !Objects.isNull(requestDTO)
                && !StringUtils.isBlank(requestDTO.getPath())
                && !StringUtils.isBlank(requestDTO.getModule())
                && !StringUtils.isBlank(requestDTO.getHttpMethod())
                && !Objects.isNull(HttpMethodEnum.acquireByName(requestDTO.getHttpMethod()));
    }
}
