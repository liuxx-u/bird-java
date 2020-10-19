package com.bird.core.trace;

/**
 * @author liuxx
 * @since 2020/10/19
 */
public interface IGlobalTraceIdProvider {

    /**
     * 获取全局traceId
     *
     * @return global trace id
     */
    String globalTraceId();
}
