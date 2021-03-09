package com.bird.core.trace.interceptor;

import com.bird.core.trace.TraceDefinition;

/**
 * @author liuxx
 * @since 2021/2/26
 */
public interface ITraceInterceptor {

    /**
     * 轨迹记录前拦截
     * @param current 当前轨迹信息
     * @param next 下一条轨迹信息
     */
    void enter(TraceDefinition current,TraceDefinition next);

    /**
     * 轨迹退出时拦截
     * @param current 当前轨迹信息
     */
    void exit(TraceDefinition current);
}
