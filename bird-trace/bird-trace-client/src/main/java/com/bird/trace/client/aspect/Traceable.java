package com.bird.trace.client.aspect;

import com.bird.trace.client.sql.TraceSQLType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Traceable {

    /**
     * 名称
     * @return value
     */
    String value() default "";

    /**
     * 记录SQL语句的类型
     * @return TraceSQLType数组
     */
    TraceSQLType[] sqlTypes() default {};
}
