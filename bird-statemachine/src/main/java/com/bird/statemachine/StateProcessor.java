package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2021/5/7
 */
@FunctionalInterface
public interface StateProcessor<C extends StateContext> {

    /**
     * execute state action
     *
     * @param context state context
     * @return  result state
     */
    String action(C context);
}
