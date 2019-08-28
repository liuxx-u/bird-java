package com.bird.service.common.mapper.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据规则元 注解
 * @author liuxx
 * @date 2018/9/30
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRule {
    /**
     * 规则元名称
     * @return 规则元名称
     */
    String name() default "";

    /**
     * 规则元值来源类型
     * @return 数据来源类型
     */
    RuleSourceStrategy strategy() default RuleSourceStrategy.TEXT;

    /**
     * 当数据来源是用户选择时{@code RuleSourceStrategy.CHOICE}
     * @return 数据地址
     */
    String url() default "";

    /**
     * 当数据来源是系统提供时{@code RuleSourceStrategy.SYSTEM}
     * @return 提供器类名
     */
    Class<? extends IDataRuleProvider> provider() default NullDataRuleProvider.class;
}
