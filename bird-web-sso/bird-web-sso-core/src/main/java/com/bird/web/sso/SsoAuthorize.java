package com.bird.web.sso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 平台单点登陆权限切面
  *
  * @author liuxx
  * @since 2020/6/30
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SsoAuthorize {

    String[] permissions() default {};

    String[] roles() default {};

    boolean isCheckAll() default false;

    boolean anonymous() default false;
}
