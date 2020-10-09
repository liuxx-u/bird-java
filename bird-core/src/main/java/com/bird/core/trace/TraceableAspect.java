package com.bird.core.trace;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Slf4j
@Aspect
public class TraceableAspect {

    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.bird.core.trace.Traceable)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();

            String entrance = signature.getDeclaringTypeName() + "#" + method.getName();
            Traceable traceable = method.getAnnotation(Traceable.class);
            String description = traceable == null ? StringUtils.EMPTY : traceable.value();

            TraceContext.enter(entrance, args, description);
        } catch (Exception ex) {
            log.error("操作日志记录失败：" + ex.getMessage());
        }
    }

    @AfterReturning(value = "logPointCut()", returning = "returnValue")
    public void afterReturning(Object returnValue) {
        TraceContext.exit(returnValue);
    }
}
