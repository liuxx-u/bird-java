package com.bird.lock.aspect;

import com.bird.lock.IDistributedLock;
import com.bird.lock.exception.DistributedLockException;
import com.bird.lock.expression.ILockKeyParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 分布式锁拦截切面
 * 切面紧跟在 {@link org.springframework.aop.interceptor.ExposeInvocationInterceptor} 之后执行
 *
 * @author liuxx
 * @since 2020/12/2
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class DistributedLockAspect {

    private final static String ERROR_MESSAGE = "系统繁忙，请稍后再试";

    private final IDistributedLock distributedLock;
    private final ILockKeyParser lockKeyParser;

    public DistributedLockAspect(IDistributedLock distributedLock, ILockKeyParser lockKeyParser) {
        this.distributedLock = distributedLock;
        this.lockKeyParser = lockKeyParser;
    }

    @Around(value = "@annotation(lockAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String lockKey = lockKeyParser.parse(lockAnnotation.key(), method.getParameters(), args);
        if (distributedLock.tryLock(lockKey, lockAnnotation.keyExpire(), lockAnnotation.retryInterval(), lockAnnotation.retryExpire())) {
            try {
                return joinPoint.proceed();
            } finally {
                distributedLock.unLock(lockKey);
            }
        } else {
            log.error("执行方法:{}获取锁超时,lockKey:{}", method.getName(), lockKey);
            throw new DistributedLockException(ERROR_MESSAGE);
        }
    }
}
