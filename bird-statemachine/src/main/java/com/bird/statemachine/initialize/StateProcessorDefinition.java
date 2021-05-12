package com.bird.statemachine.initialize;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.initialize.annotation.StateHandler;
import lombok.Data;

/**
 * @author liuxx
 * @since 2021/5/11
 */
@Data
@SuppressWarnings("rawtypes")
public class StateProcessorDefinition {

    /**
     * source state{@link State} name
     */
    private String state;

    /**
     * event {@link Event} name
     */
    private String event;

    /**
     * scene id
     */
    private String sceneId;

    /**
     * state processor
     */
    private StateProcessor stateProcessor;

    public StateProcessorDefinition(){}

    public StateProcessorDefinition(StateHandler stateHandler,StateProcessor stateProcessor){
        this.state = stateHandler.state();
        this.event = stateHandler.event();
        this.sceneId = stateHandler.sceneId();
        this.stateProcessor = stateProcessor;
    }
}
