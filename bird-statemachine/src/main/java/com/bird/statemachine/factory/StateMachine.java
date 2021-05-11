package com.bird.statemachine.factory;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;

/**
 * state machine
 *
 * @author liuxx
 * @since 2021/5/7
 */
public interface StateMachine<S extends State,E extends Event,C extends StateContext> {

    /**
     * get machine id
     *
     * @return machine id
     */
    String getMachineId();

    /**
     * set machine ready status
     *
     * @param ready ready status
     */
    void setReady(boolean ready);

    /**
     * fire state event
     *
     * @param sourceState  source state
     * @param event        event
     * @param stateContext state context
     * @return target state
     */
    S fireEvent(S sourceState, E event, C stateContext);
}
