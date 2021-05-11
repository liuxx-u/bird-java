package com.bird.statemachine.scene;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class DefaultSceneStateProcessor<S extends State, C extends StateContext>  implements SceneStateProcessor<S,C> {

    private final String sceneId;
    private final StateProcessor<S, C> processor;

    public DefaultSceneStateProcessor(String sceneId, StateProcessor<S, C> processor) {
        this.sceneId = sceneId;
        this.processor = processor;
    }

    @Override
    public S action(C context) {
        return this.processor.action(context);
    }

    @Override
    public String getSceneId() {
        return this.sceneId;
    }
}
