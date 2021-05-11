package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public interface On<S extends State, E extends Event, C extends StateContext> {


    /**
     * build state event by event name
     *
     * @param eventName event name
     * @return On clause builder
     */
    When<S,C> on(String eventName);

    /**
     * Build state event
     *
     * @param event transition event
     * @return On clause builder
     */
    default When<S, C> on(E event){
        return on(event.getName());
    }
}
