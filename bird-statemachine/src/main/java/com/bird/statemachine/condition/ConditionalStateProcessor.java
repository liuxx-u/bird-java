package com.bird.statemachine.condition;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

/**
 *
 *
 * @author liuxx
 * @since 2021/5/7
 */
public interface ConditionalStateProcessor<S extends State, C extends StateContext> extends StateProcessor<S,C> {

    public static int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    public static int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * priority of condition judgment
     *
     * @return priority
     */
    default int getPriority() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * judge whether the condition is satisfied
     *
     * @param ctx state context
     * @return condition judgement
     */
    boolean judge(C ctx);
}
