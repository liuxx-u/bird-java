package com.bird.statemachine.scene;

import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class DefaultSceneStateProcessor<C extends StateContext>  implements SceneStateProcessor<C> {

    private final String sceneId;
    private final StateProcessor<C> processor;

    public DefaultSceneStateProcessor(String sceneId, StateProcessor<C> processor) {
        this.sceneId = sceneId;
        this.processor = processor;
    }

    @Override
    public String action(C context) {
        return this.processor.action(context);
    }

    @Override
    public String getSceneId() {
        return this.sceneId;
    }
}
