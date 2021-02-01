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
    DEFAULT;

    /**
     * 判断新增时是否忽略
     * @param saveStrategy 保存策略
     * @return 是否忽略新增
     */
    public static boolean isIgnoreInsert(SaveStrategyEnum saveStrategy){
        return saveStrategy == IGNORE || saveStrategy == INSERT_IGNORE;
    }

    /**
     * 判断编辑时是否忽略
     * @param saveStrategy 保存策略
     * @return 是否忽略编辑
     */
    public static boolean isIgnoreUpdate(SaveStrategyEnum saveStrategy){
        return saveStrategy == IGNORE || saveStrategy == UPDATE_IGNORE;
    }
}
