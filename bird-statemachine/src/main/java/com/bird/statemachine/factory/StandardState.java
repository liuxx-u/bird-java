package com.bird.statemachine.factory;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.exception.StateMachineException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public class StandardState<S extends State,E extends Event,C extends StateContext> {

    private final String stateId;

    private final Map<E, StateProcessor<S, C>> eventMap;

    public StandardState(String stateId, Map<E, StateProcessor<S, C>> eventMap) {
        this.stateId = stateId;
        this.eventMap = eventMap == null ? new HashMap<>() : eventMap;
    }

    /**
     * execute event processor on current state
     *
     * @param event event
     * @param ctx   state context
     * @return target state
     */
    public S process(E event, C ctx) {
        StateProcessor<S, C> stateProcessor = this.obtainProcessor(event);
        if (stateProcessor == null) {
            throw new StateMachineException("no such event processor on state : " + this.stateId);
        }
        return stateProcessor.action(ctx);
    }

    public String getStateId() {
        return this.stateId;
    }

    /**
     * set event processor on current state
     *
     * @param event     event
     * @param processor processor
     */
    public void setProcessor(E event, StateProcessor<S, C> processor) {
        if (event == null) {
            throw new StateMachineException("event can`t be null");
        }
        if (processor == null) {
            throw new StateMachineException("processor can`t be null");
        }
        if (this.eventMap.containsKey(event)) {
            throw new StateMachineException(event + " already exist, you can not add another one");
        }
        this.eventMap.put(event, processor);
    }

    /**
     * obtain the event processor of current state
     *
     * @param event event
     * @return processor
     */
    public StateProcessor<S, C> obtainProcessor(E event) {
        return eventMap.get(event);
    }
}
