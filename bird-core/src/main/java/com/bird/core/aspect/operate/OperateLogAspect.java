package com.bird.core.aspect.operate;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author liuxx
 * @date 2019/1/15
 */
@Slf4j
@Aspect
public class OperateLogAspect {
    private OperateLogBuffer logBuffer = new OperateLogBuffer();

    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.bird.core.aspect.operate.OperateLog)")
    public void logPointCut() {
    }

    @AfterReturning("logPointCut()")
    public void log(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            OperateLogInfo logInfo = new OperateLogInfo(method,args);
            logBuffer.enqueue(logInfo);
        } catch (Exception ex) {
            log.error("操作日志记录失败：" + ex.getMessage());
        }
    }
}
