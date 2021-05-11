package com.bird.statemachine.factory;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.exception.StateMachineException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @since 2021/5/8
 */
public class StandardStateMachine<S extends State,E extends Event,C extends StateContext> implements StateMachine<S,E,C> {

    private final String machineId;
    private final Map<String, StandardState<S, E, C>> stateMap;

    private boolean ready;

    public StandardStateMachine(String machineId, List<StandardState<S, E, C>> states) {
        this.machineId = machineId;
        this.stateMap = states.stream().collect(Collectors.toMap(StandardState::getStateId, s -> s, (oldValue, newValue) -> newValue));
    }


    @Override
    public String getMachineId() {
        return this.machineId;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public S fireEvent(S sourceState, E event, C stateContext) {
        this.checkReady();

        StandardState<S, E, C> state = this.getState(sourceState);
        if (state == null) {
            throw new StateMachineException("source state");
        }
        return state.process(event, stateContext);
    }

    private void checkReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    private StandardState<S, E, C> getState(S state) {
        if (state == null) {
            return null;
        }
        return this.stateMap.get(state.getName());
    }
}
