package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public interface StateContext {

    /**
     * scene
     *
     * @return scene id
     */
    default String getSceneId(){
        return "DEFAULT";
    }
}
