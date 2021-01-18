package com.bird.service.common.grid;

/**
 * @author liuxx
 * @since 2021/1/18
 */
public @interface GridField {

    /**
     * 字段名对应的select
     */
    String select() default "";
}
