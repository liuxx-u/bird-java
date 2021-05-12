package com.bird.statemachine.scene;

import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.condition.ConditionalStateProcessor;
import com.bird.statemachine.exception.StateMachineException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class SceneStateProcessorCluster <C extends StateContext> implements StateProcessor<C> {

    private final Map<String, SceneStateProcessor<C>> sceneProcessorMap;

    public SceneStateProcessorCluster() {
        sceneProcessorMap = new HashMap<>();
    }

    /**
     * add conditional state processor
     *
     * @param processor {@link ConditionalStateProcessor}
     */
    public void addScene(SceneStateProcessor<C> processor) {
        if (processor == null) {
            throw new StateMachineException("scene processor can not be null.");
        }

        String sceneId = processor.getSceneId();
        if (StringUtils.isEmpty(sceneId)) {
            sceneId = StateContext.DEFAULT_SCENE;
        }
        if (this.sceneProcessorMap.containsKey(sceneId)) {
            throw new StateMachineException("scene processor[" + sceneId + "] already exist, you can not add another one");
        }
        this.sceneProcessorMap.put(sceneId, processor);
    }

    @Override
    public String action(C context) {
        String sceneId = context.getSceneId();
        if (StringUtils.isEmpty(sceneId)) {
            sceneId = StateContext.DEFAULT_SCENE;
        }

        SceneStateProcessor<C> stateProcessor = sceneProcessorMap.get(sceneId);
        if (stateProcessor == null) {
            throw new StateMachineException("scene processor[" + sceneId + "] is null, exit.");
        }
        return stateProcessor.action(context);
    }
}
