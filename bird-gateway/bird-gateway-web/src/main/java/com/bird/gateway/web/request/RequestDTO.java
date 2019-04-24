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
     * 服务名
     */
    private String module;

    /**
     * 总路径
     */
    private String path;


    /**
     * 不带服务名的子路径
     */
    private String subUrl;

    /**
     * RPC方式
     */
    private String rpcType;

    /**
     * 请求方式，仅支持GET、POST
     */
    private String httpMethod;

    /**
     * 从ServerHttpRequest 解析请求信息 .
     *
     * @param httpRequest {@linkplain ServerHttpRequest}
     * @return request dto
     */
    public static RequestDTO transform(final ServerHttpRequest httpRequest) {
        final String path = httpRequest.getPath().value();
        Optional<String> module = Arrays.stream(path.split("/")).filter(StringUtils::isNotBlank).findFirst();
        final String rpcType = httpRequest.getHeaders().getFirst(Constants.RPC_TYPE);
        final String httpMethod = httpRequest.getMethod() == null ? HttpMethodEnum.POST.getName() : httpRequest.getMethod().name();

        RequestDTO request = new RequestDTO();
        module.ifPresent(p -> {
            request.setModule(p);
            String subUrl = path.substring(p.length() + 1);
            if (StringUtils.isNotBlank(httpRequest.getURI().getQuery())) {
                subUrl += "?" + httpRequest.getURI().getQuery();
            }
            request.setSubUrl(subUrl);
        });
        request.setPath(path);
        request.setHttpMethod(httpMethod);
        request.setRpcType(rpcType);

        return request;
    }
}
