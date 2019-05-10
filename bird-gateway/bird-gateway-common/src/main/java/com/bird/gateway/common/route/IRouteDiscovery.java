package com.bird.gateway.common.route;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public interface IRouteDiscovery {

    /**
     * 根据路径获取对应的路由信息
     * @param path path
     * @return route
     */
    RouteDefinition get(String path);
}
