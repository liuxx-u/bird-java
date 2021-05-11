package com.bird.statemachine.builder;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.condition.ConditionalStateProcessor;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public interface When<S extends State, C extends StateContext> {

    /**
     * Define action to be performed during transition without condition
     *
     * @param processor processor to be performed
     * @return When
     */
    When<S, C> perform(StateProcessor<S, C> processor);

    /**
     * Define action to be performed during transition with lowest priority
     *
     * @param condition the condition of processor
     * @param processor processor to be performed
     * @return When
     */
    default When<S, C> perform(Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        return perform(ConditionalStateProcessor.LOWEST_PRECEDENCE, condition, processor);
    }

    /**
     * Define action to be performed during transition
     *
     * @param priority  the priority of processor
     * @param condition the condition of processor
     * @param processor processor to be performed
     * @return When
     */
    When<S, C> perform(int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor);

    /**
     * Define action to be performed in different scene
     *
     * @param sceneId   scene id
     * @param processor processor to be performed
     * @return When
     */
    When<S, C> perform(String sceneId, StateProcessor<S, C> processor);
}
