package com.bird.web.common.interceptor.signature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证接口签名的注解
 *
 * @author liuxx
 * @date 2019/7/23
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureCheck {

    boolean ignore() default false;
}
