package com.bird.statemachine.builder;

import com.bird.statemachine.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class TransitionBuilder<S,E,C> implements When<S,C>, On<S,E,C> {

    private State<S, E, C> source;

    E event;

    private Transition<S, E, C> transition;

    final Map<S, State<S, E, C>> stateMap;

    TransitionBuilder(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    public On<S, E, C> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public When<S, C> on(E event) {
        this.event = event;
        return this;
    }

    @Override
    public synchronized When<S, C> perform(int priority, Function<C, Boolean> condition, Function<C, S> action) {
        if (condition == null || action == null) {
            throw new StateMachineException("perform condition and action can`t be null");
        }
        if (this.transition == null) {
            transition = this.source.setTransition(this.event, new Transition<>(this.source, this.event));
        }
        transition.addAction(new Action<>(priority, condition, action));
        return this;
    }
}
