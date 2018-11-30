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
     * RPC pipe enum.
     */
    RPC(50,"rpc"),

    /**
     * Rewrite pipe enum.
     */
    REWRITE(51, "rewrite"),

    /**
     * Redirect pipe enum.
     */
    REDIRECT(52, "redirect"),

    /**
     * Divide pipe enum.
     */
    DIVIDE(53, "divide"),

    /**
     * Dubbo pipe enum.
     */
    DUBBO(54, "dubbo"),

    /**
     * springCloud pipe enum.
     */
    SPRING_CLOUD(55, "springCloud"),

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
