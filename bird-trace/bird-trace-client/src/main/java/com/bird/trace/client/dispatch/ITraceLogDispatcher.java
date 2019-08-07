package com.bird.trace.client.dispatch;

import com.bird.trace.client.TraceLog;

import java.util.Collection;

/**
 * 日志发送器
 *
 * 发出日志，可使用队列接收，批量保存
 *
 * @author liuxx
 * @date 2019/8/4
 */
public interface ITraceLogDispatcher {

    /**
     * 发送跟踪日志
     * @param traceLogs 跟踪日志
     */
    void dispatch(Collection<TraceLog> traceLogs);
}
