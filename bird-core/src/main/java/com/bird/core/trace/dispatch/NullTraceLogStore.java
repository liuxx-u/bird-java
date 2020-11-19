package com.bird.core.trace.dispatch;

import com.bird.core.trace.TraceDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class NullTraceLogStore implements ITraceLogStore {

    /**
     * 存储轨迹信息
     *
     * @param traceLogs 轨迹信息
     */
    @Override
    public void store(List<TraceDefinition> traceLogs) {
        log.warn("未注入ITraceLogStore实例，丢弃跟踪日志信息");
    }
}
