package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.factory.StandardState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class StandardStatesBuilder<S extends State, E extends Event, C extends StateContext> extends StandardStateBuilder<S,E,C> {

    private final List<StandardState<S, E, C>> sources = new ArrayList<>();

    StandardStatesBuilder(Map<S, StandardState<S, E, C>> stateMap) {
        super(stateMap);
    }

    public On<S, E, C> fromAmong(S... stateIds) {
        for (S stateId : stateIds) {
            sources.add(this.stateMap.computeIfAbsent(stateId, p -> new StandardState<>(stateId.getName(), new HashMap<>(4))));
        }
        return this;
    }

    @Override
    public When<S, C> perform(StateProcessor<S, C> processor) {
        this.sources.forEach(state -> super.performProcessor(state, processor));
        return this;
    }

    @Override
    public synchronized When<S, C> perform(int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        this.sources.forEach(state -> super.performConditionProcessor(state, priority, condition, processor));
        return this;
    }

    @Override
    public synchronized When<S, C> perform(String sceneId, StateProcessor<S, C> processor) {
        this.sources.forEach(state -> super.performSceneProcessor(state, sceneId, processor));
        return this;
    }
}
