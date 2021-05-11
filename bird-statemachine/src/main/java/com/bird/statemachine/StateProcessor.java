package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2021/5/7
 */
@FunctionalInterface
public interface StateProcessor<S extends State,C extends StateContext> {

    /**
     * execute state action
     *
     * @param context state context
     * @return  result state
     */
    S action(C context);
}
