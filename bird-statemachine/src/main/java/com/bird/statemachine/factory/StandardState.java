package com.bird.statemachine.factory;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.exception.StateMachineException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public class StandardState<S extends State,C extends StateContext> {

    private final String stateId;

    private final Map<String, StateProcessor<S, C>> eventMap;

    public StandardState(String stateId, Map<String, StateProcessor<S, C>> eventMap) {
        this.stateId = stateId;
        this.eventMap = eventMap == null ? new HashMap<>() : eventMap;
    }

    /**
     * execute event processor on current state
     *
     * @param eventName event name
     * @param ctx       state context
     * @return target state
     */
    public S process(String eventName, C ctx) {
        StateProcessor<S, C> stateProcessor = this.obtainProcessor(eventName);
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
     * @param eventName event {@link Event} name
     * @param processor processor
     */
    public void setProcessor(String eventName, StateProcessor<S, C> processor) {
        if (StringUtils.isEmpty(eventName)) {
            throw new StateMachineException("event name can`t be empty");
        }
        if (processor == null) {
            throw new StateMachineException("processor can`t be null");
        }
        if (this.eventMap.containsKey(eventName)) {
            throw new StateMachineException(eventName + " already exist, you can not add another one");
        }
        this.eventMap.put(eventName, processor);
    }

    /**
     * obtain the event processor of current state
     *
     * @param eventName event name
     * @return processor
     */
    public StateProcessor<S, C> obtainProcessor(String eventName) {
        return eventMap.get(eventName);
    }
}
