package com.bird.eventbus.handler;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/1/16
 */
public interface IEventHandlerStore {

    /**
     * 注册事件消费者信息，注意去重
     * @param definitions 消费者信息
     */
    void initialize(List<EventHandlerDefinition> definitions);

    /**
     * 存储事件消费结果
     * @param results 结果信息
     */
    void store(List<EventHandleResult> results);
}
