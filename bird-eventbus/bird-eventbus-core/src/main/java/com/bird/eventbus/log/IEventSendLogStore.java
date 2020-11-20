package com.bird.eventbus.log;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public interface IEventSendLogStore {

    /**
     * 存储事件发送结果
     * @param logs 结果信息
     */
    void storeSendLogs(List<EventSendLog> logs);
}
