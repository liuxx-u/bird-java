package com.bird.core.trace.dispatch;

import com.bird.core.trace.TraceDefinition;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/10/9
 */
public interface ITraceLogStore {

    /**
     * 存储轨迹信息
     * @param traceLogs 轨迹信息
     */
    void store(List<TraceDefinition> traceLogs);
}
