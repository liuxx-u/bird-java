package com.bird.trace.skywalking.request;

import com.bird.core.trace.IGlobalTraceIdProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Slf4j
public class SkywalkingGlobalTraceIdProvider implements IGlobalTraceIdProvider {
    /**
     * 获取全局traceId
     *
     * @return global trace id
     */
    @Override
    public String globalTraceId() {
        return org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId();
    }
}
