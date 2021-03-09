package com.bird.core.trace;

import com.bird.core.SpringContextHolder;
import com.bird.core.trace.dispatch.ITraceLogDispatcher;
import com.bird.core.trace.interceptor.ITraceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Slf4j
public class TraceContext {

    private static final ThreadLocal<TraceMap> LOCAL = ThreadLocal.withInitial(TraceMap::new);

    private TraceContext() {
    }

    /**
     * 获取当前操作的轨迹信息
     *
     * @return trace
     */
    public static TraceDefinition current() {
        TraceMap traceMap = LOCAL.get();
        return traceMap.current();
    }

    /**
     * 获取当前操作的trace id
     *
     * @return trace id
     */
    public static String currentTraceId() {
        TraceDefinition traceDefinition = current();
        return traceDefinition == null ? StringUtils.EMPTY : traceDefinition.getTraceId();
    }

    /**
     * 进入
     *
     * @param entrance    入口
     * @param params      参数
     * @param description 描述
     */
    public static void enter(String entrance, Object[] params, String description) {

        TraceDefinition current = current();
        TraceDefinition next = current == null ? TraceDefinition.initWithDefault(entrance, params) : current.next(entrance, params);
        next.setDescription(description);

        // 轨迹记录前拦截操作
        try {
            ITraceInterceptor interceptor = SpringContextHolder.getBean(ITraceInterceptor.class);
            interceptor.enter(current, next);
        } catch (Exception ignore) {
        }

        TraceMap traceMap = LOCAL.get();
        traceMap.setCurrentTraceId(next.getTraceId());
        traceMap.put(next.getTraceId(), next);
    }

    /**
     * 获取当前轨迹信息中的摘要信息
     *
     * @param key 摘要Key
     * @return value
     */
    public static Object getClaim(String key) {
        TraceDefinition current = current();
        if (current == null) {
            return null;
        }
        return current.getClaim(key);
    }

    /**
     * 向轨迹信息中设置摘要信息
     *
     * @param key   摘要Key
     * @param value value
     */
    public static void setClaim(String key, Object value) {
        TraceDefinition current = current();
        if (current == null) {
            log.warn("当前跟踪信息为NULL");
            return;
        }
        current.setClaim(key, value);
    }

    /**
     * 退出方法
     *
     * @param returnValue 返回值
     */
    public static void exit(Object returnValue) {

        TraceDefinition current = current();
        if (current == null) {
            log.warn("当前跟踪信息为NULL");
            return;
        }
        current.setEndTime(System.currentTimeMillis());
        if (returnValue != null) {
            current.setReturnValue(returnValue);
        }

        TraceMap traceMap = LOCAL.get();
        if (traceMap == null) {
            return;
        }

        // 轨迹退出时拦截操作
        try {
            ITraceInterceptor interceptor = SpringContextHolder.getBean(ITraceInterceptor.class);
            interceptor.exit(current);
        } catch (Exception ignore) {
        }

        TraceDefinition parent = traceMap.get(current.getParentTraceId());
        if (parent != null) {
            traceMap.setCurrentTraceId(parent.getTraceId());
        } else {
            clear();
        }
    }

    /**
     * 退出方法并强制清除线程信息
     *
     * @param returnValue 返回值
     */
    public static void exitAndClear(Object returnValue) {
        exit(returnValue);
        clear();
    }

    /**
     * 清除轨迹信息
     */
    private static void clear() {
        try {
            Collection<TraceDefinition> traceLogs = LOCAL.get().values();
            if (CollectionUtils.isEmpty(traceLogs)) {
                return;
            }

            ITraceLogDispatcher dispatcher = SpringContextHolder.getBean(ITraceLogDispatcher.class);
            dispatcher.dispatch(traceLogs);
        } catch (Exception ex) {
            log.error("轨迹信息保存失败", ex);
        }

        LOCAL.remove();
    }
}
