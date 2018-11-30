package com.bird.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Getter
@RequiredArgsConstructor
public enum ResultEnum {

    /**
     * Success result enum.
     */
    SUCCESS("success"),

    /**
     * Time out result enum.
     */
    TIME_OUT("timeOut"),

    /**
     * Error result enum.
     */
    ERROR("error"),;

    private final String name;
}
