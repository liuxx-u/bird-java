package com.bird.service.common.trace.interceptor;

import com.bird.service.common.trace.ColumnTraceProperties;
import com.bird.service.common.trace.DatabaseOperateEnum;
import com.bird.service.common.trace.IDatabaseOperateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;
import java.util.Map;

/**
 * @author shaojie
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class,method = "update",args = {Statement.class})
})
public class MybatisColumnTraceInterceptor implements Interceptor {

    private Map<String, IDatabaseOperateHandler> handlers;
    private boolean enabled;

    public MybatisColumnTraceInterceptor(Map<String, IDatabaseOperateHandler> handlers) {
        this.handlers = handlers;
    }


    public MybatisColumnTraceInterceptor(ColumnTraceProperties properties) {
        enabled = properties.isEnabled();
        this.handlers = DatabaseOperateEnum.operateHandlers(null);
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(enabled){
            Statement statement = (Statement) invocation.getArgs()[0];
            String sql = resolveSql(statement);
            String operate = sql.substring(0, sql.indexOf(" ")).toUpperCase();
            IDatabaseOperateHandler handler = handlers.get(operate);
            if(handler  != null){
                handler.record(statement.getConnection(),sql,operate);
            }else{
                log.warn("Current update sql {} cannot find operateHandler, will skip operate record.",sql);
            }
        }
        // 调用实际的方法
        return invocation.proceed();
    }

    private String resolveSql(Statement statement) {
        String stmt = statement.toString();
        int i = stmt.indexOf(":");
        return i > 0 ? stmt.substring(i + 1).trim() : stmt.trim();
    }
}
