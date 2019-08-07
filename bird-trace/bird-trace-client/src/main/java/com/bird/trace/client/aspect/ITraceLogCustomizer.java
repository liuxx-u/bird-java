package com.bird.trace.client.aspect;

import com.bird.trace.client.TraceLog;

/**
 * @author liuxx
 * @date 2019/8/7
 */
public interface ITraceLogCustomizer {

    /**
     * Customize the given a {@link TraceLog} object.
     *
     * @param log the log object to customize
     */
    void customize(TraceLog log);
}
