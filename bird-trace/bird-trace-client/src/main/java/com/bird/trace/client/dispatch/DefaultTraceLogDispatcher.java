package com.bird.trace.client.dispatch;

import com.bird.trace.client.TraceLog;

import java.util.Collection;

/**
 * @author liuxx
 * @date 2019/8/7
 */
public class DefaultTraceLogDispatcher implements ITraceLogDispatcher {
    /**
     * 发送跟踪日志
     *
     * @param traceLogs 跟踪日志
     */
    @Override
    public void dispatch(Collection<TraceLog> traceLogs) {
        System.out.println("enter");
    }
}
