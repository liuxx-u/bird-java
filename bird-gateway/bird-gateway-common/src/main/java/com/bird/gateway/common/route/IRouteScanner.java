package com.bird.gateway.common.route;

import java.util.List;

/**
 * 定义路由扫描器
 *
 * @author liuxx
 * @date 2019/5/6
 */
public interface IRouteScanner {

    /**
     * 获取路由信息
     * @return list
     */
    List<RouteDefinition> getRoutes();
}
