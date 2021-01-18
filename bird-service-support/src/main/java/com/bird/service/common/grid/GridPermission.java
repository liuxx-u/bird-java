package com.bird.service.common.grid;

/**
 * @author liuxx
 * @since 2021/1/18
 */
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
