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
     * Build transition event
     *
     * @param event transition event
     * @return On clause builder
     */
    When<S, C> on(E event);
}
