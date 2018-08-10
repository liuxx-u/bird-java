package com.bird.web.common.interceptor.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等性验证注解
 *
 * @author liuxx
 * @date 2018/7/23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotency {

    /**
     * 是否强制验证请求头
     * @return
     */
    boolean force() default false;
}
