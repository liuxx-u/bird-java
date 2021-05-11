package com.bird.statemachine.factory;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.exception.StateMachineException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2021/5/10
 */
@SuppressWarnings("unchecked")
public final class StateMachineFactory {

    private final static ConcurrentHashMap<String, StateMachine> STATE_MACHINE_MAP = new ConcurrentHashMap<>();


    /**
     * register state machine
     *
     * @param stateMachine state machine
     */
    public static <S extends State, E extends Event, C extends StateContext> void register(StateMachine<S, E, C> stateMachine) {
        String machineId = stateMachine.getMachineId();
        if (STATE_MACHINE_MAP.get(machineId) != null) {
            throw new StateMachineException("The state machine with id [" + machineId + "] is already built, no need to build again");
        }
        STATE_MACHINE_MAP.put(stateMachine.getMachineId(), stateMachine);
    }

    /**
     * get state machine by id
     *
     * @param machineId machine id
     * @return state machine
     */
    public static <S extends State, E extends Event, C extends StateContext> StateMachine<S, E, C> get(String machineId) {
        StateMachine<S, E, C> stateMachine = STATE_MACHINE_MAP.get(machineId);
        if (stateMachine == null) {
            throw new StateMachineException("There is no stateMachine instance for " + machineId + ", please build it first");
        }
        return stateMachine;
    }
}
