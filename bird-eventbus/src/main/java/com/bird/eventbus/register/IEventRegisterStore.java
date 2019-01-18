package com.bird.eventbus.register;

/**
 * @author liuxx
 * @date 2019/1/17
 */
public interface IEventRegisterStore {

    /**
     * 存储事件发布结果
     * @param result 发布结果
     */
    void register(EventRegisterResult result);
}
