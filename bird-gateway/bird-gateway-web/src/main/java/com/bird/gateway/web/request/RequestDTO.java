package com.bird.gateway.web.request;

import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.enums.HttpMethodEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Data
public class RequestDTO implements Serializable {

    /**
     * is module data.
     */
    private String module;

    /**
     * is path with module
     */
    private String path;

    /**
     *  httpMethod now we only support "get","post" .
     * {@linkplain  HttpMethodEnum}
     */
    private String httpMethod;

    /**
     * this is sign .
     */
    private String sign;

    /**
     * timestamp .
     */
    private String timestamp;

    /**
     * appKey .
     */
    private String appKey;

    /**
     * ServerHttpRequest transform RequestDTO .
     *
     * @param request {@linkplain ServerHttpRequest}
     * @return RequestDTO request dto
     */
    public static RequestDTO transform(final ServerHttpRequest request) {
        final String path = request.getPath().value();
        Optional<String> module = Arrays.stream(path.split("/")).filter(StringUtils::isNotBlank).findFirst();

        final String appKey = request.getHeaders().getFirst(Constants.APP_KEY);
        final String httpMethod = request.getMethod().name();
        final String sign = request.getHeaders().getFirst(Constants.SIGN);
        final String timestamp = request.getHeaders().getFirst(Constants.TIMESTAMP);

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setPath(path);
        module.ifPresent(requestDTO::setModule);
        requestDTO.setAppKey(appKey);
        requestDTO.setHttpMethod(httpMethod);
        requestDTO.setSign(sign);
        requestDTO.setTimestamp(timestamp);

        return requestDTO;
    }

}
