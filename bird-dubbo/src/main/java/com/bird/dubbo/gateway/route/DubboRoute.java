package com.bird.dubbo.gateway.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dubbo 网关路由注解
 * @author liuxx
 * @date 2018/11/15
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboRoute {

    /**
     * 路径
     *
     * @return 映射的网关路径
     */
    String path() default "";

    /**
     * 描述
     * @return 网关描述信息
     */
    String desc() default "";

    /**
     * 所需权限名
     * @return 权限名集合
     */
    String[] permissions() default {};

    /**
     * 是否检查全部权限
     * @return true:表示需要满足所有权限;false:标识只要满足其中一条权限即可
     */
    boolean isCheckAll() default false;

    /**
     * 是否可以匿名访问
     * @return true：表示可以匿名访问
     */
    boolean anonymous() default false;
}
