package com.bird.trace.client.aspect;

import com.bird.trace.client.TraceContext;
import lombok.extern.slf4j.Slf4j;
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
    @Pointcut("@annotation(com.bird.trace.client.aspect.Traceable)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            TraceContext.enter(method, args);

        } catch (Exception ex) {
            log.error("操作日志记录失败：" + ex.getMessage());
        }
    }

    @AfterReturning("logPointCut()")
    public void afterReturning(JoinPoint joinPoint){

    }
}
