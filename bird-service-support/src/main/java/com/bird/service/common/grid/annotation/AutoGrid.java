package com.bird.service.common.grid.annotation;

import com.bird.service.common.grid.enums.DbFieldMode;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.executor.DialectType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动表格 注解
 *
 * 每个注解标记的类对应一个数据表格的增删改查
 *
 * @author liuxx
 * @since 2021/1/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoGrid {

    /**
     * 查询的名称
     */
    String name();

    /**
     * 执行器类型
     */
    DialectType dialectType() default DialectType.MYSQL;

    /**
     * from ：SQL的from部分
     */
    String from();

    /**
     * 主表，多表关联时用于指定新增、删除数据的表
     */
    String mainTable() default "";

    /**
     * 主键列
     */
    String primaryKey() default "id";

    /**
     * where ：SQL的where部分，不包含前端传递的查询条件
     */
    String where() default "";

    /**
     * appendSql ：附加的SQL语句
     */
    String appendSql() default "";

    /**
     * 默认的排序字段
     */
    String defaultSortField() default "id";

    /**
     * 默认的排序方式
     */
    SortDirectionEnum defaultSortDirection() default SortDirectionEnum.DESC;

    /**
     * 数据库字段模式，默认：与表格字段相同
     */
    DbFieldMode dbFeildMode() default DbFieldMode.SAME;
}
