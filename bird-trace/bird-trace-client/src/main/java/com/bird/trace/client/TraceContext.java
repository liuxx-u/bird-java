package com.bird.trace.client;

import com.alibaba.fastjson.JSON;
import com.bird.trace.client.dispatch.ITraceLogDispatcher;
import com.bird.trace.client.sql.TraceSQL;
import com.bird.trace.client.sql.TraceSQLType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Slf4j
public class TraceContext {

    private static final ThreadLocal<ThreadTraceLog> THREAD_TRACE_LOG = ThreadLocal.withInitial(ThreadTraceLog::new);

    private static ITraceLogDispatcher logDispatcher;

    private TraceContext() {
    }

    /**
     * 设置日志发送器
     *
     * @param dispatcher dispatcher
     */
    public static void init(ITraceLogDispatcher dispatcher) {
        logDispatcher = dispatcher;
    }

    /**
     * 获取当前操作的日志记录
     *
     * @return trace log
     */
    public static TraceLog current() {
        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        if (StringUtils.isBlank(local.currentKey) || !local.traceLogMap.containsKey(local.currentKey)) {
            return null;
        }
        return local.traceLogMap.get(local.currentKey);
    }

    /**
     * 进入方法
     *
     * @param log log
     */
    public static void enter(TraceLog log) {
        if (logDispatcher == null || log == null) {
            return;
        }

        TraceLog current = current();
        if (current != null) {
            log.setParentKey(current.getKey());
        }
        String key = UUID.randomUUID().toString();
        log.setKey(key);

        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        local.traceLogMap.put(key, log);
        local.currentKey = key;
    }

    /**
     * 附加SQL语句
     *
     * @param sql sql
     */
    public static void appendSQL(TraceSQL sql) {
        if (logDispatcher == null) {
            return;
        }

        if (sql == null || StringUtils.isBlank(sql.getSql())) {
            return;
        }
        TraceLog current = current();
        if (current == null) {
            log.warn("当前跟踪信息为NULL");
            return;
        }
        current.appendSQL(sql);
    }

    /**
     * 是否记录指定类型的SQL语句
     *
     * @param sqlType sqlType
     * @return true or false
     */
    public static boolean isSupport(TraceSQLType sqlType) {
        TraceLog current = current();
        if (current == null || sqlType == TraceSQLType.NONE || CollectionUtils.isEmpty(current.getSqlTypes())) {
            return false;
        }
        return current.getSqlTypes().contains(sqlType);
    }

    /**
     * 退出方法
     *
     * @param returnValue 返回值
     */
    public static void exit(Object returnValue) {
        if (logDispatcher == null) {
            return;
        }

        TraceLog current = current();
        if (current == null) {
            log.warn("当前跟踪信息为NULL");
            return;
        }
        current.setEndTime(new Date());
        if (returnValue != null) {
            current.setReturnValue(JSON.toJSONString(returnValue));
        }

        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        if (StringUtils.isNotBlank(current.getParentKey())) {
            local.currentKey = current.getParentKey();
        } else {
            logDispatcher.dispatch(local.traceLogMap.values());
            THREAD_TRACE_LOG.remove();
        }
    }

    @Data
    private static class ThreadTraceLog {

        /**
         * 当前的key
         */
        private String currentKey;
        /**
         * key-trace map
         */
        private Map<String, TraceLog> traceLogMap;

        private ThreadTraceLog() {
            traceLogMap = new HashMap<>();
        }
    }
}
