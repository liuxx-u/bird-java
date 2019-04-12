package com.bird.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Getter
@RequiredArgsConstructor
public enum RpcTypeEnum {

    /**
     * Http rpc type enum.
     */
    HTTP("http", true),

    /**
     * Dubbo rpc type enum.
     */
    DUBBO("dubbo", true),


    /**
     * springCloud rpc type enum.
     */
    SPRING_CLOUD("springCloud", true);


    private final String name;

    private final Boolean support;


    /**
     * acquire operator supports.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupports() {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * acquireByName.
     *
     * @param name this is rpc type
     * @return RpcTypeEnum
     */
    public static RpcTypeEnum acquireByName(final String name) {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support && e.name.equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }
}
