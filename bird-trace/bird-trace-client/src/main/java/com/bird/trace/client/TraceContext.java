package com.bird.trace.client;

import com.bird.trace.client.sql.TraceSQL;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
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

    /**
     * 获取当前操作的日志记录
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
     * @param method 方法
     * @param args 方法参数
     */
    public static void enter(Method method, Object[] args){
        TraceLog log = new TraceLog(method,args);

        TraceLog current = current();
        if (current != null){
            log.setParentKey(current.getKey());
        }
        String key = UUID.randomUUID().toString();
        log.setKey(key);

        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        local.traceLogMap.put(key,log);
        local.currentKey = key;
    }

    /**
     * 附加SQL语句
     * @param sql sql
     */
    public static void appendSQL(TraceSQL sql) {
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
     * 退出方法
     */
    public static void exit(){
        TraceLog current = current();
        if (current == null) {
            log.warn("当前跟踪信息为NULL");
            return;
        }
        current.setEndTime(new Date());

        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        if(StringUtils.isNotBlank(current.getParentKey())){
            local.currentKey = current.getParentKey();
        }else {
            // dispatch local.traceLogMap.values();
            THREAD_TRACE_LOG.remove();
        }
    }

    @Data
    private static class ThreadTraceLog{

        /**
         * 当前的key
         */
        private String currentKey;
        /**
         * key-trace map
         */
        private Map<String,TraceLog> traceLogMap;

        private ThreadTraceLog(){
            traceLogMap = new HashMap<>();
        }
    }
}
