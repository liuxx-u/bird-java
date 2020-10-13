package com.bird.core.trace;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Getter
@RequiredArgsConstructor
public enum TraceTypeEnum {

    /**
     * 默认
     */
    DEFAULT("default"),
    /**
     * skywalking
     */
    SKYWALKING("skywalking");

    private final String name;
}
