package com.bird.service.common.grid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxx
 * @since 2021/1/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GridPermission {

    /**
     * 权限名前缀
     */
    String prefix() default "";

    /**
     * 数据获取权限
     */
    String read() default "";

    /**
     * 数据新增权限
     */
    String add() default "";

    /**
     * 数据编辑权限
     */
    String edit() default "";

    /**
     * 数据删除权限
     */
    String delete() default "";
}
