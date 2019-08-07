package com.bird.trace.client.aspect;

import com.bird.trace.client.TraceContext;
import com.bird.trace.client.TraceLog;
import com.bird.trace.client.sql.TraceSQLType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Slf4j
@Aspect
public class TraceableAspect {

    private List<TraceSQLType> defaultSQLTypes;
    private List<ITraceLogCustomizer> logCustomizers;

    public void setDefaultSQLTypes(List<TraceSQLType> defaultSQLTypes) {
        this.defaultSQLTypes = defaultSQLTypes;
    }

    public void setLogCustomizers(List<ITraceLogCustomizer> logCustomizers) {
        this.logCustomizers = logCustomizers;
    }


    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.bird.trace.client.aspect.Traceable)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();

            TraceLog log = new TraceLog(method, args, defaultSQLTypes);
            if (!CollectionUtils.isEmpty(this.logCustomizers)) {
                for (ITraceLogCustomizer logCustomizer : this.logCustomizers) {
                    logCustomizer.customize(log);
                }
            }
            TraceContext.enter(log);
        } catch (Exception ex) {
            log.error("操作日志记录失败：" + ex.getMessage());
        }
    }

    @AfterReturning(value = "logPointCut()", returning = "returnValue")
    public void afterReturning(Object returnValue) {
        TraceContext.exit(returnValue);
    }
}
