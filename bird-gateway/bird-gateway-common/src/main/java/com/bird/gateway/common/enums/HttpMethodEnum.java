package com.bird.gateway.common.enums;

import com.bird.gateway.common.exception.GatewayException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Getter
@RequiredArgsConstructor
public enum HttpMethodEnum {

    /**
     * Get http method enum.
     */
    GET("get", true),

    /**
     * Post http method enum.
     */
    POST("post", true),

    /**
     * Put http method enum.
     */
    PUT("put", false),

    /**
     * Delete http method enum.
     */
    DELETE("delete", false);

    private final String name;

    private final Boolean support;

    /**
     *  convert by name.
     *
     * @param name name
     * @return {@link HttpMethodEnum }
     */
    public static HttpMethodEnum acquireByName(final String name) {
        return Arrays.stream(HttpMethodEnum.values())
                .filter(e -> e.support && e.name.equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new GatewayException(" this http method can not support!"));
    }

}
