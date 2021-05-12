package com.bird.statemachine.builder;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.condition.ConditionalStateProcessorCluster;
import com.bird.statemachine.condition.DefaultConditionStateProcessor;
import com.bird.statemachine.exception.StateMachineException;
import com.bird.statemachine.factory.StandardState;
import com.bird.statemachine.scene.DefaultSceneStateProcessor;
import com.bird.statemachine.scene.SceneStateProcessorCluster;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public class StandardStateBuilder<C extends StateContext> implements When<C>, On<C> {

    private StandardState<C> source;

    String eventName;

    final Map<String, StandardState<C>> stateMap;

    StandardStateBuilder(Map<String, StandardState<C>> stateMap) {
        this.stateMap = stateMap;
    }

    public On<C> from(State state) {
        return from(state.getName());
    }

    public On<C> from(String stateName) {
        this.source = this.stateMap.computeIfAbsent(stateName, p -> new StandardState<>(stateName, new HashMap<>(4)));
        return this;
    }

    @Override
    public When<C> on(String eventName) {
        this.eventName = eventName;
        return this;
    }

    @Override
    public synchronized When<C> perform(StateProcessor<C> processor) {
        this.performProcessor(this.source, processor);
        return this;
    }

    @Override
    public synchronized When<C> perform(int priority, Function<C, Boolean> condition, StateProcessor<C> processor) {
        this.performConditionProcessor(this.source, priority, condition, processor);
        return this;
    }

    @Override
    public synchronized When<C> perform(String sceneId, StateProcessor<C> processor) {
        this.performSceneProcessor(this.source, sceneId, processor);
        return this;
    }

    protected void performProcessor(StandardState<C> sourceState, StateProcessor<C> processor) {
        sourceState.setProcessor(this.eventName, processor);
    }

    protected void performConditionProcessor(StandardState<C> sourceState, int priority, Function<C, Boolean> condition, StateProcessor<C> processor) {
        if (this.eventName == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (condition == null || processor == null) {
            throw new StateMachineException("perform condition and processor can`t be null");
        }

        ConditionalStateProcessorCluster<C> conditionalProcessorCluster;
        StateProcessor<C> stateProcessor = sourceState.obtainProcessor(this.eventName);
        if (stateProcessor == null) {
            conditionalProcessorCluster = new ConditionalStateProcessorCluster<>();
            sourceState.setProcessor(this.eventName, conditionalProcessorCluster);
        } else if (stateProcessor instanceof ConditionalStateProcessorCluster) {
            conditionalProcessorCluster = (ConditionalStateProcessorCluster<C>) stateProcessor;
        } else {
            throw new StateMachineException(this.eventName+ " already exist, you can not add another one");
        }

        conditionalProcessorCluster.addCondition(new DefaultConditionStateProcessor<>(priority, condition, processor));
    }

    protected void performSceneProcessor(StandardState<C> sourceState, String sceneId, StateProcessor<C> processor) {
        if (this.eventName == null) {
            throw new StateMachineException("current event is null, please call method [on] before.");
        }
        if (processor == null) {
            throw new StateMachineException("perform processor can`t be null");
        }
        if(StringUtils.isEmpty(sceneId)){
            this.performProcessor(sourceState,processor);
        }else {
            SceneStateProcessorCluster<C> sceneProcessorCluster;
            StateProcessor<C> stateProcessor = sourceState.obtainProcessor(this.eventName);
            if (stateProcessor == null) {
                sceneProcessorCluster = new SceneStateProcessorCluster<>();
                sourceState.setProcessor(this.eventName, sceneProcessorCluster);
            } else if (stateProcessor instanceof SceneStateProcessorCluster) {
                sceneProcessorCluster = (SceneStateProcessorCluster<C>) stateProcessor;
            } else {
                throw new StateMachineException(eventName + " already exist, you can not add another one");
            }
            sceneProcessorCluster.addScene(new DefaultSceneStateProcessor<>(sceneId, processor));
        }
    }
}
