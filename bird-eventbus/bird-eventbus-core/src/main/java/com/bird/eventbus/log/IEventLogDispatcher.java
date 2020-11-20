package com.bird.eventbus.log;

/**
 * @author liuxx
 * @since 2020/11/20
 */
public interface IEventLogDispatcher {

    /**
     * 发送事件日志
     *
     * @param eventLog 事件日志
     */
    void dispatch(AbstractEventLog eventLog);
}
