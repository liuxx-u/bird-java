package com.bird.eventbus.log;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public class NullEventLogStore implements IEventSendLogStore,IEventHandleLogStore {

    /**
     * 存储事件发送结果
     *
     * @param logs 结果信息
     */
    @Override
    public void storeSendLogs(List<EventSendLog> logs) {

    }

    /**
     * 存储事件消费结果
     *
     * @param logs 结果信息
     */
    @Override
    public void storeHandleLogs(List<EventHandleLog> logs) {

    }
}
