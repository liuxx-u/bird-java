package com.bird.eventbus.log;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/1/16
 */
public interface IEventHandleLogStore {

    /**
     * 存储事件消费结果
     * @param logs 结果信息
     */
    void storeHandleLogs(List<EventHandleLog> logs);
}
