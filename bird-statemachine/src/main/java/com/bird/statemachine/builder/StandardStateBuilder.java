package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.condition.ConditionalStateProcessor;
import com.bird.statemachine.condition.InternalConditionalStateProcessor;
import com.bird.statemachine.condition.LambdaConditionStateProcessor;
import com.bird.statemachine.exception.StateMachineException;
import com.bird.statemachine.factory.StandardState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public class StandardStateBuilder<S extends State, E extends Event, C extends StateContext> implements When<S,C>, On<S,E,C> {

    private StandardState<S, E, C> source;

    E event;

    final Map<S, StandardState<S, E, C>> stateMap;

    StandardStateBuilder(Map<S, StandardState<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    public On<S, E, C> from(S stateId) {
        this.source = this.stateMap.computeIfAbsent(stateId, p -> new StandardState<>(stateId.getName(), new HashMap<>(4)));
        return this;
    }

    @Override
    public When<S, C> on(E event) {
        this.event = event;
        return this;
    }

    @Override
    public synchronized When<S, C> perform(StateProcessor<S, C> processor) {
        this.performProcessor(this.source, processor);
        return this;
    }

    @Override
    public synchronized When<S, C> perform(int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        this.performConditionProcessor(this.source, priority, condition, processor);
        return this;
    }

    protected void performProcessor(StandardState<S, E, C> sourceState, StateProcessor<S, C> processor) {
        sourceState.setProcessor(this.event, processor);
    }

    protected void performConditionProcessor(StandardState<S, E, C> sourceState, int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        if (this.event == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (condition == null || processor == null) {
            throw new StateMachineException("perform condition and processor can`t be null");
        }

        InternalConditionalStateProcessor<S, C> conditionalStateProcessor;
        StateProcessor<S, C> stateProcessor = sourceState.obtainProcessor(this.event);
        if (stateProcessor == null) {
            conditionalStateProcessor = new InternalConditionalStateProcessor<>();
            sourceState.setProcessor(this.event, conditionalStateProcessor);
        } else if (stateProcessor instanceof InternalConditionalStateProcessor) {
            conditionalStateProcessor = (InternalConditionalStateProcessor<S, C>) stateProcessor;
        } else {
            throw new StateMachineException(event + " already exist, you can not add another one");
        }

        conditionalStateProcessor.addCondition(new LambdaConditionStateProcessor<>(priority, condition, processor));
    }
}
