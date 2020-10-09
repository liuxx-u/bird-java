package com.bird.core.trace.dispatch;

import com.bird.core.trace.TraceDefinition;

import java.util.Collection;

/**
 * 日志发送器
 *
 * 发出日志，可使用队列接收，批量保存
 *
 * @author liuxx
 * @since 2020/10/9
 */
public interface ITraceLogDispatcher {
    /**
     * 发送跟踪日志
     * @param traceLogs 跟踪日志
     */
    void dispatch(Collection<TraceDefinition> traceLogs);
}
