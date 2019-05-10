package com.bird.gateway.common.route;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public interface IRouteRegistry {

    /**
     * 注册模块的路由信息集合
     * @param module module
     * @param routeDefinitions route list
     */
    void put(String module, List<RouteDefinition> routeDefinitions);
}
