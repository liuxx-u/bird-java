package com.bird.statemachine.builder;

import com.bird.statemachine.Action;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public interface When<S, C> {

    /**
     * Define action to be performed during transition without condition
     *
     * @param action performed action
     * @return When
     */
    default When<S, C> perform(Function<C, S> action) {
        return perform(p -> true, action);
    }

    /**
     * Define action to be performed during transition with lowest priority
     *
     * @param condition condition
     * @param action    performed action
     * @return When
     */
    default When<S, C> perform(Function<C, Boolean> condition, Function<C, S> action) {
        return perform(Action.LOWEST_PRECEDENCE, condition, action);
    }

    /**
     * Define action to be performed during transition
     *
     * @param priority  performed priority
     * @param condition performed condition
     * @param action    performed action
     * @return When
     */
    When<S, C> perform(int priority, Function<C, Boolean> condition, Function<C, S> action);
}
