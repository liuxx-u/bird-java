package com.bird.gateway.registry.zookeeper.discovery;

import com.bird.gateway.common.IRouteDiscovery;
import com.bird.gateway.common.RouteDefinition;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class ZookeeperRouteDiscovery implements IRouteDiscovery {


    /**
     * 根据路径获取对应的路由信息
     *
     * @param path path
     * @return route
     */
    @Override
    public RouteDefinition get(String path) {
        return ZookeeperCacheManager.get(path);
    }
}
