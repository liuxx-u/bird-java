package com.bird.statemachine.builder;

import com.bird.statemachine.*;

import java.util.Map;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class TransitionBuilder<S,E,C> implements From<S,E,C>, On<S,E,C>, To<S,E,C> {

    private State<S, E, C> source;

    State<S, E, C> target;

    private Transition<S, E, C> transition;

    final Map<S, State<S, E, C>> stateMap;
    final TransitionType transitionType;

    TransitionBuilder(Map<S, State<S, E, C>> stateMap, TransitionType transitionType){
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    public From<S, E, C> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    public To<S, E, C> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public On<S, E, C> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public When<S, E, C> when(Condition<C> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        transition.setAction(action);
    }
}
