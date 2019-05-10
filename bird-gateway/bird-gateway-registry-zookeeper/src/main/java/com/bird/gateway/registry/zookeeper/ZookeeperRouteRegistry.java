package com.bird.gateway.registry.zookeeper;

import com.bird.gateway.common.route.IRouteRegistry;
import com.bird.gateway.common.route.RouteDefinition;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class ZookeeperRouteRegistry implements IRouteRegistry {

    private final ZkClient zkClient;

    public ZookeeperRouteRegistry(ZkClient zkClient){
        this.zkClient = zkClient;
    }

    /**
     * 注册模块的路由信息集合
     *
     * @param module module
     * @param routeDefinitions 网关路由信息集合
     */
    @Override
    public void put(String module, List<RouteDefinition> routeDefinitions) {
        if(CollectionUtils.isNotEmpty(routeDefinitions)){
            for(RouteDefinition route : routeDefinitions){
                if (route == null || StringUtils.isBlank(route.getPath())) continue;
                this.syncZkNode(route);
            }
        }
    }

    /**
     * 同步zookeeper节点信息
     * @param route 路由信息
     */
    private void syncZkNode(RouteDefinition route){
        String routePath = ZkPathConstant.buildZkRoutePath(route.getModule(),route.getPath());
        if (!zkClient.exists(routePath)) {
            zkClient.createPersistent(routePath, true);
        }
        zkClient.writeData(routePath, route);
    }
}
