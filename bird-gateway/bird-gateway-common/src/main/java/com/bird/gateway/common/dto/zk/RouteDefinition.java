package com.bird.gateway.common.dto.zk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Data
@ToString
@EqualsAndHashCode
public class RouteDefinition implements Serializable {

    /**
     * 模块
     */
    private String module;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否启用
     */
    private Boolean enabled;
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
    private Boolean checkAll;
    /**
     * 转发方式
     */
    private String rpcType;
    /**
     * JSON参数（根据转发方式不同灵活配置）
     */
    private String rpcJson;
}
