package com.bird.statemachine.builder;

import com.bird.statemachine.Condition;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public interface StateTransition<S,E,C> {

    /**
     * 源状态
     *
     * @return 源状态
     */
    S from();

    /**
     * 目标状态
     *
     * @return 目标状态
     */
    S to();

    /**
     * 事件
     *
     * @return 事件
     */
    E event();

    /**
     * 过渡条件
     *
     * @return 过渡条件
     */
    default Condition<C> condition() {
        return context -> true;
    }

    /**
     * 执行方法
     */
    Function<C, S> action();
}
