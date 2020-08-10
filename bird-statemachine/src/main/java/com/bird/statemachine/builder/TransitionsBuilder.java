package com.bird.statemachine.builder;

import com.bird.statemachine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class TransitionsBuilder<S,E,C> extends TransitionBuilder<S,E,C> {

    private List<State<S, E, C>> sources = new ArrayList<>();

    private List<Transition<S, E, C>> transitions = new ArrayList<>();

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
    public When<S, C> on(E event) {
        this.event = event;
        return this;
    }
}
