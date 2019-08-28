package com.bird.eventbus.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2019/1/17
 */
@Getter
@RequiredArgsConstructor
public enum  EventHandleStatusEnum {

    SUCCESS(0, "成功"),

    PARTIAL_SUCCESS(1, "部分成功"),

    FAIL(2, "失败"),

    TIMEOUT(-1, "超时");

    private final Integer code;
    private final String name;
}
