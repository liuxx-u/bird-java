package com.bird.trace.client.dispatch;

import com.bird.trace.client.TraceLog;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/7
 */
public interface IDefaultTraceLogStore {

    /**
     * 存储跟踪日志信息
     * @param logs 日志集合
     */
    void store(List<TraceLog> logs);
}
