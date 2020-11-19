package com.bird.eventbus.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2019/1/17
 */
@Getter
@RequiredArgsConstructor
public enum  EventHandleStatusEnum {

    /**
     * 成功
     */
    SUCCESS("SUCCESS", "成功"),
    /**
     * 部分成功
     */
    PARTIAL_SUCCESS("PARTIAL_SUCCESS", "部分成功"),
    /**
     * 失败
     */
    FAIL("FAIL", "失败"),
    /**
     * 超时
     */
    TIMEOUT("TIMEOUT", "超时");

    private final String code;
    private final String name;
}
