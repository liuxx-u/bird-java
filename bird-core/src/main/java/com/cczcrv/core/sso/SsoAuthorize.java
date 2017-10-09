package com.bird.core.sso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bird平台单点登陆权限切面
 * Created by liuxx on 2017/5/17.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SsoAuthorize {

    String[] permissions() default {};
}
