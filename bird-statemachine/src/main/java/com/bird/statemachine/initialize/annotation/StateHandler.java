package com.bird.statemachine.initialize.annotation;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;

import java.lang.annotation.*;

/**
 * @author liuxx
 * @since 2021/5/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface StateHandler {

    /**
     * state machine id
     *
     * @return machine id
     */
    String machineId();

    /**
     * source state{@link State} name
     *
     * @return state name
     */
    String state();

    /**
     * event {@link Event} name
     *
     * @return event name
     */
    String event();

    /**
     * scene id
     *
     * @return scene id
     */
    String sceneId() default "";
}
