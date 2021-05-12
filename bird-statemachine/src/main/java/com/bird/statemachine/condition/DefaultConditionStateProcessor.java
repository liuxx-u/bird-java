package com.bird.statemachine.condition;

import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class DefaultConditionStateProcessor<C extends StateContext> implements ConditionalStateProcessor<C> {

    private final int priority;
    private final Function<C, Boolean> condition;
    private final StateProcessor<C> action;

    public DefaultConditionStateProcessor(Function<C, Boolean> condition, StateProcessor<C> action) {
        this(ConditionalStateProcessor.LOWEST_PRECEDENCE, condition, action);
    }

    public DefaultConditionStateProcessor(int priority, Function<C, Boolean> condition, StateProcessor<C> action) {
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
    public String action(C context) {
        return this.action.action(context);
    }
}
