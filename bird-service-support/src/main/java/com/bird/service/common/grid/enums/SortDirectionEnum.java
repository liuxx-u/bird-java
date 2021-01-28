package com.bird.service.common.grid.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @since 2021/1/28
 */
@Getter
@RequiredArgsConstructor
public enum  SortDirectionEnum {
    /**
     * 正序
     */
    ASC(0),
    /**
     * 倒序
     */
    DESC(1);


    private final int code;
}
