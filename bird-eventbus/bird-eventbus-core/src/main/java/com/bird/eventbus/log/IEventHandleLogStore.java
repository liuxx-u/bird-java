package com.bird.eventbus.log;

import com.bird.eventbus.log.EventHandleLog;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/1/16
 */
public interface IEventHandleLogStore {

    /**
     * 存储事件消费结果
     * @param results 结果信息
     */
    void store(List<EventHandleLog> results);
}
