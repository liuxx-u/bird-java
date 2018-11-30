package com.bird.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Getter
@RequiredArgsConstructor
public enum  LoadBalanceEnum {
    /**
     * Hash load balance enum.
     */
    HASH(1, "hash", true),

    /**
     * Random load balance enum.
     */
    RANDOM(2, "random", true),

    /**
     * Round robin load balance enum.
     */
    ROUND_ROBIN(3, "roundRobin", true);

    private final int code;

    private final String name;

    private final boolean support;
}
