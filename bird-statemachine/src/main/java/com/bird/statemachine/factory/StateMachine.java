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
public interface StateMachine<C extends StateContext> {

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
     * @param stateName  source state name
     * @param eventName    event name
     * @param stateContext state context
     * @return target state name
     */
    String fireEvent(String stateName, String eventName, C stateContext);

    /**
     * fire state event
     *
     * @param sourceState  source state
     * @param event        event
     * @param stateContext state context
     * @return target state
     */
    default String fireEvent(State sourceState, Event event, C stateContext) {
        return this.fireEvent(sourceState.getName(), event.getName(), stateContext);
    }
}
