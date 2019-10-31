package com.bird.service.common.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAspect {

    @Around("@within(com.bird.service.common.datasource.TargetDataSource) || @annotation(com.bird.service.common.datasource.TargetDataSource)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String currentDataSource = DataSourceContext.get();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Class clazz= signature.getDeclaringType();
        Method method = signature.getMethod();
        // 先判断方法上面是否有注解
        if(method.isAnnotationPresent(TargetDataSource.class)){
            TargetDataSource targetDataSource = method.getAnnotation(TargetDataSource.class);
            String value = targetDataSource.value();
            if(StringUtils.isNotEmpty(value)){
                // 设置value到数据源上下文中
                DataSourceContext.set(value);
            }
        } else if(clazz.isAnnotationPresent(TargetDataSource.class)){
            // 类上面是否有注解
            TargetDataSource targetDataSource = (TargetDataSource) clazz.getAnnotation(TargetDataSource.class);
            String value = targetDataSource.value();
            if(StringUtils.isNotEmpty(value)){
                // 设置value 到数据源上下文中
                DataSourceContext.set(value);
            }
        }
        // 执行方法
        Object proceed = point.proceed();
        // 还原进入切面时的数据源上下文
        DataSourceContext.set(currentDataSource);
        return proceed;
    }
}
