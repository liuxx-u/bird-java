package com.bird.web.common.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略JsonWrapper包裹器的注解
 * @author liuxx
 * @date 2019/6/21
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonWrapperIgnore {
}
