package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public interface StateContext {

    final static String DEFAULT_SCENE = "DEFAULT";

    /**
     * scene
     *
     * @return scene id
     */
    default String getSceneId() {
        return DEFAULT_SCENE;
    }
}
