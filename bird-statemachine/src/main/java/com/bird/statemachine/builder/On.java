package com.bird.statemachine.builder;


/**
 * @author liuxx
 * @since 2020/8/7
 */
public interface On<S,E, C>{
    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    When<S, C> on(E event);
}
