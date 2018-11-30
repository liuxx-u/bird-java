package com.bird.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Getter
@RequiredArgsConstructor
public enum PipeTypeEnum {

    /**
     * before pipe type enum.
     */
    BEFORE("before"),

    /**
     * function pipe type enum.
     */
    FUNCTION("function"),

    /**
     * last pipe type enum.
     */
    LAST("last");

    private final String name;
}