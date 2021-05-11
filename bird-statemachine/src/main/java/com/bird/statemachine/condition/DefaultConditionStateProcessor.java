package com.bird.statemachine.condition;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class DefaultConditionStateProcessor<S extends State, C extends StateContext> implements ConditionalStateProcessor<S,C> {

    private final int priority;
    private final Function<C, Boolean> condition;
    private final StateProcessor<S, C> action;

    public DefaultConditionStateProcessor(Function<C, Boolean> condition, StateProcessor<S, C> action) {
        this(ConditionalStateProcessor.LOWEST_PRECEDENCE, condition, action);
    }

    public DefaultConditionStateProcessor(int priority, Function<C, Boolean> condition, StateProcessor<S, C> action) {
        this.priority = priority;
        this.condition = condition;
        this.action = action;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean judge(C ctx) {
        return this.condition.apply(ctx);
    }

    @Override
    public S action(C context) {
        return this.action.action(context);
    }
}
