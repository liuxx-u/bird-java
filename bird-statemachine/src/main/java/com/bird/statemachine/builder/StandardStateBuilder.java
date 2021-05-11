package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.condition.ConditionalStateProcessorCluster;
import com.bird.statemachine.condition.DefaultConditionStateProcessor;
import com.bird.statemachine.exception.StateMachineException;
import com.bird.statemachine.factory.StandardState;
import com.bird.statemachine.scene.DefaultSceneStateProcessor;
import com.bird.statemachine.scene.SceneStateProcessorCluster;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public class StandardStateBuilder<S extends State, E extends Event, C extends StateContext> implements When<S,C>, On<S,E,C> {

    private StandardState<S, C> source;

    String eventName;

    final Map<String, StandardState<S, C>> stateMap;

    StandardStateBuilder(Map<String, StandardState<S, C>> stateMap) {
        this.stateMap = stateMap;
    }

    public On<S, E, C> from(S state) {
        return from(state.getName());
    }

    public On<S, E, C> from(String stateName) {
        this.source = this.stateMap.computeIfAbsent(stateName, p -> new StandardState<>(stateName, new HashMap<>(4)));
        return this;
    }

    @Override
    public When<S, C> on(String eventName) {
        this.eventName = eventName;
        return this;
    }

    @Override
    public synchronized When<S, C> perform(StateProcessor<S, C> processor) {
        this.performProcessor(this.source, processor);
        return this;
    }

    @Override
    public synchronized When<S, C> perform(int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        this.performConditionProcessor(this.source, priority, condition, processor);
        return this;
    }

    @Override
    public synchronized When<S, C> perform(String sceneId, StateProcessor<S, C> processor) {
        this.performSceneProcessor(this.source, sceneId, processor);
        return this;
    }

    protected void performProcessor(StandardState<S, C> sourceState, StateProcessor<S, C> processor) {
        sourceState.setProcessor(this.eventName, processor);
    }

    protected void performConditionProcessor(StandardState<S, C> sourceState, int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        if (this.eventName == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (condition == null || processor == null) {
            throw new StateMachineException("perform condition and processor can`t be null");
        }

        ConditionalStateProcessorCluster<S, C> conditionalProcessorCluster;
        StateProcessor<S, C> stateProcessor = sourceState.obtainProcessor(this.eventName);
        if (stateProcessor == null) {
            conditionalProcessorCluster = new ConditionalStateProcessorCluster<>();
            sourceState.setProcessor(this.eventName, conditionalProcessorCluster);
        } else if (stateProcessor instanceof ConditionalStateProcessorCluster) {
            conditionalProcessorCluster = (ConditionalStateProcessorCluster<S, C>) stateProcessor;
        } else {
            throw new StateMachineException(this.eventName+ " already exist, you can not add another one");
        }

        conditionalProcessorCluster.addCondition(new DefaultConditionStateProcessor<>(priority, condition, processor));
    }

    protected void performSceneProcessor(StandardState<S, C> sourceState, String sceneId, StateProcessor<S, C> processor) {
        if (this.eventName == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (processor == null) {
            throw new StateMachineException("perform processor can`t be null");
        }

        SceneStateProcessorCluster<S, C> sceneProcessorCluster;
        StateProcessor<S, C> stateProcessor = sourceState.obtainProcessor(this.eventName);
        if (stateProcessor == null) {
            sceneProcessorCluster = new SceneStateProcessorCluster<>();
            sourceState.setProcessor(this.eventName, sceneProcessorCluster);
        } else if (stateProcessor instanceof SceneStateProcessorCluster) {
            sceneProcessorCluster = (SceneStateProcessorCluster<S, C>) stateProcessor;
        } else {
            throw new StateMachineException(eventName + " already exist, you can not add another one");
        }
        sceneProcessorCluster.addScene(new DefaultSceneStateProcessor<>(sceneId, processor));
    }
}
