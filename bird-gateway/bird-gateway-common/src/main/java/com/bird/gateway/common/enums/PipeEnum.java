package com.bird.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Getter
@RequiredArgsConstructor
public enum PipeEnum {
    /**
     * Pre pipe enum.
     */
    PRE(1, "pre"),

    /**
     * Waf pipe enum.
     */
    WAF(10,"waf"),

    /**
     * Sign pipe enum.
     */
    SIGN(20, "sign"),

    /**
     * Auth pipe enum.
     */
    AUTH(30, "auth"),

    /**
     * Rate limiter pipe enum.
     */
    RATE_LIMIT(40, "rate_limit"),


    /**
     * Divide pipe enum.
     */
    DIVIDE(50, "divide"),

    /**
     * RPC pipe enum.
     */
    RPC(60,"rpc"),

    /**
     * Rewrite pipe enum.
     */
    HTTP(61, "rewrite"),

    /**
     * Dubbo pipe enum.
     */
    DUBBO(62, "dubbo"),

    /**
     * springCloud pipe enum.
     */
    SPRING_CLOUD(63, "springCloud"),

    /**
     * Monitor pipe enum.
     */
    MONITOR(100, "monitor"),

    /**
     * Result pipe enum
     */
    RESULT(2147483647,"result");

    private final int code;

    private final String name;
}
