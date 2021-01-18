package com.bird.service.common.grid;

/**
 * 自动表格 注解
 *
 * 每个注解标记的类对应一个数据表格的增删改查
 *
 * @author liuxx
 * @since 2021/1/18
 */
public @interface AutoGrid {

    /**
     * from ：SQL的from部分
     */
    String from() default "";

    /**
     * where ：SQL的where部分，不包含前端传递的查询条件
     */
    String where() default "";

    /**
     * appendSql ：附加的SQL语句
     */
    String appendSql() default "";
}
