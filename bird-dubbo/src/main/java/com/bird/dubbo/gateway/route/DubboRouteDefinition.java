package com.bird.dubbo.gateway.route;

import lombok.Getter;
import lombok.Setter;

/**
 * Dubbo网关信息
 * @author liuxx
 * @date 2018/11/15
 */
@Getter
@Setter
public class DubboRouteDefinition {

    /**
     * 应用名
     */
    private String application;
    /**
     * 接口名称
     */
    private String interfaceClass;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数名与类型JSON字符串
     */
    private String parameters;
    /**
     * 版本
     */
    private String version;

    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否可以匿名访问
     */
    private Boolean anonymous;
    /**
     * 所需权限名
     */
    private String permissions;
    /**
     * 是否检查全部权限
     */
    private Boolean isCheckAll;
    /**
     * 生成增删查改对应DTO的class名
     */
    private String crudClass;
}
