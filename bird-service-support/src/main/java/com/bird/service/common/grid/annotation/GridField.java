package com.bird.service.common.grid.annotation;

import com.bird.service.common.grid.GridFieldType;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import com.bird.service.common.grid.enums.SaveStrategyEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxx
 * @since 2021/1/18
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GridField {

    /**
     * 数据源对应的字段名
     */
    String dbField() default "";

    /**
     * 对应的数据源字段类型
     */
    GridFieldType fieldType() default GridFieldType.NULL;

    /**
     * 数据保存策略
     */
    SaveStrategyEnum saveStrategy() default SaveStrategyEnum.DEFAULT;

    /**
     * 字段查询策略
     */
    QueryStrategyEnum queryStrategy() default QueryStrategyEnum.ALLOW;
}
