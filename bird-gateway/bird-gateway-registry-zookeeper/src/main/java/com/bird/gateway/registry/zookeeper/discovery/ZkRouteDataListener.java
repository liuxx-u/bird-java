package com.bird.gateway.registry.zookeeper.discovery;

import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.registry.zookeeper.ZkPathConstant;
import org.I0Itec.zkclient.IZkDataListener;

import java.util.Optional;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class ZkRouteDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String zkPath, Object data) {
        Optional.ofNullable(data).ifPresent(o -> {
            RouteDefinition dto = (RouteDefinition) o;
            String routePath = ZkPathConstant.extractRoutePath(zkPath);
            ZookeeperCacheManager.ROUTE_MAP.put(routePath, dto);
        });
    }

    @Override
    public void handleDataDeleted(String zkPath) {
        String routePath = ZkPathConstant.extractRoutePath(zkPath);
        ZookeeperCacheManager.ROUTE_MAP.remove(routePath);
    }
}
