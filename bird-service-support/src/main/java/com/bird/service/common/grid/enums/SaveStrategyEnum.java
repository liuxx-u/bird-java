package com.bird.service.common.grid.enums;

import lombok.Getter;

/**
 * 数据保存策略
 *
 * @author liuxx
 * @since 2021/1/27
 */
@Getter
public enum SaveStrategyEnum {
    /**
     * 新增/编辑时都忽略
     */
    IGNORE,
    /**
     * 新增时忽略
     */
    INSERT_IGNORE,
    /**
     * 编辑时忽略
     */
    UPDATE_IGNORE,
    /**
     * 编辑时值为NULL时忽略
     */
    UPDATE_NULL_IGNORE,
    /**
     * 新增/编辑时都更新
     */
    DEFAULT
}
