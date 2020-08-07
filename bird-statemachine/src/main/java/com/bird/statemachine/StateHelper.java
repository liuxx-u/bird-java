package com.bird.statemachine;

import java.util.Map;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class StateHelper {

    public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId) {
        State<S, E, C> state = stateMap.get(stateId);
        if (state == null) {
            state = new State<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }
}
