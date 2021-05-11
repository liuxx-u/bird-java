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

    private StandardState<S, E, C> source;

    E event;

    final Map<S, StandardState<S, E, C>> stateMap;

    StandardStateBuilder(Map<S, StandardState<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    public On<S, E, C> from(S stateId) {
        this.source = this.stateMap.computeIfAbsent(stateId, p -> new StandardState<>(stateId.getName(), new HashMap<>(4)));
        return this;
    }

    @Override
    public When<S, C> on(E event) {
        this.event = event;
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

    protected void performProcessor(StandardState<S, E, C> sourceState, StateProcessor<S, C> processor) {
        sourceState.setProcessor(this.event, processor);
    }

    protected void performConditionProcessor(StandardState<S, E, C> sourceState, int priority, Function<C, Boolean> condition, StateProcessor<S, C> processor) {
        if (this.event == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (condition == null || processor == null) {
            throw new StateMachineException("perform condition and processor can`t be null");
        }

        ConditionalStateProcessorCluster<S, C> conditionalProcessorCluster;
        StateProcessor<S, C> stateProcessor = sourceState.obtainProcessor(this.event);
        if (stateProcessor == null) {
            conditionalProcessorCluster = new ConditionalStateProcessorCluster<>();
            sourceState.setProcessor(this.event, conditionalProcessorCluster);
        } else if (stateProcessor instanceof ConditionalStateProcessorCluster) {
            conditionalProcessorCluster = (ConditionalStateProcessorCluster<S, C>) stateProcessor;
        } else {
            throw new StateMachineException(event + " already exist, you can not add another one");
        }

        conditionalProcessorCluster.addCondition(new DefaultConditionStateProcessor<>(priority, condition, processor));
    }

    protected void performSceneProcessor(StandardState<S, E, C> sourceState, String sceneId, StateProcessor<S, C> processor) {
        if (this.event == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (processor == null) {
            throw new StateMachineException("perform processor can`t be null");
        }

        SceneStateProcessorCluster<S, C> sceneProcessorCluster;
        StateProcessor<S, C> stateProcessor = sourceState.obtainProcessor(this.event);
        if (stateProcessor == null) {
            sceneProcessorCluster = new SceneStateProcessorCluster<>();
            sourceState.setProcessor(this.event, sceneProcessorCluster);
        } else if (stateProcessor instanceof SceneStateProcessorCluster) {
            sceneProcessorCluster = (SceneStateProcessorCluster<S, C>) stateProcessor;
        } else {
            throw new StateMachineException(event + " already exist, you can not add another one");
        }
        sceneProcessorCluster.addScene(new DefaultSceneStateProcessor<>(sceneId, processor));
    }
}
