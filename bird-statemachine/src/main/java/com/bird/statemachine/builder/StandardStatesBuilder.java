package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.factory.StandardState;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class StandardStatesBuilder<S extends State, E extends Event, C extends StateContext> extends StandardStateBuilder<S,E,C> {

    private final List<StandardState<S, C>> sources = new ArrayList<>();

    StandardStatesBuilder(Map<String, StandardState<S, C>> stateMap) {
        super(stateMap);
    }

    public On<S, E, C> fromAmong(S... states) {
        String[] stateNames = Arrays.stream(states).map(State::getName).collect(Collectors.toList()).toArray(new String[]{});
        return fromAmong(stateNames);
    }

    public On<S, E, C> fromAmong(String... stateNames) {
        for (String stateName : stateNames) {
            sources.add(this.stateMap.computeIfAbsent(stateName, p -> new StandardState<>(stateName, new HashMap<>(4))));
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
