package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.StateContext;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public interface On<C extends StateContext> {


    /**
     * build state event by event name
     *
     * @param eventName event name
     * @return On clause builder
     */
    When<C> on(String eventName);

    /**
     * Build state event
     *
     * @param event transition event
     * @return On clause builder
     */
    default When<C> on(Event event){
        return on(event.getName());
    }
}
