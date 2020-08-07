package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public interface Condition<C> {

    /**
     * 校验条件是否满足
     * @param context 上下文
     * @return 条件是否满足
     */
    boolean isSatisfied(C context);

    /**
     * 条件名称
     * @return 名称
     */
    default String name(){
        return this.getClass().getSimpleName();
    }
}
