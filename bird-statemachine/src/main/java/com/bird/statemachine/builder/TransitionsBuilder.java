package com.bird.statemachine.builder;

import com.bird.statemachine.*;

import java.util.*;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class TransitionsBuilder<S,E,C> extends TransitionBuilder<S,E,C> {

    private List<State<S, E, C>> sources = new ArrayList<>();
    private Map<State<S, E, C>, Transition<S, E, C>> transitionMap = new HashMap<>();

    TransitionsBuilder(Map<S, State<S, E, C>> stateMap) {
        super(stateMap);
    }

    public On<S, E, C> fromAmong(S... stateIds) {
        for (S stateId : stateIds) {
            sources.add(StateHelper.getState(super.stateMap, stateId));
        }
        return this;
    }

    @Override
    public synchronized When<S, C> perform(int priority, Function<C, Boolean> condition, Function<C, S> action) {
        if (condition == null || action == null) {
            throw new StateMachineException("perform condition and action can`t be null");
        }
        for (State<S, E, C> state : this.sources) {
            Transition<S, E, C> transition = this.transitionMap.get(state);
            if (transition == null) {
                transition = state.setTransition(this.event, new Transition<>(state, this.event));
                this.transitionMap.put(state, transition);
            }
            transition.addAction(new Action<>(priority, condition, action));
        }
        return this;
    }
}
