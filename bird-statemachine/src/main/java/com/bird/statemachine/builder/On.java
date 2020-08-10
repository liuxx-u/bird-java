package com.bird.statemachine.builder;


/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S,E, C>{
    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    When<S, C> on(E event);
}
