package com.bird.gateway.web.zookeeper;

import com.bird.gateway.common.constant.ZkPathConstants;
import com.bird.gateway.common.route.RouteDefinition;
import org.I0Itec.zkclient.IZkDataListener;

import java.util.Optional;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public class ZkRouteDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String zkPath, Object data) throws Exception {
        Optional.ofNullable(data).ifPresent(o -> {
            RouteDefinition dto = (RouteDefinition) o;
            String routePath = ZkPathConstants.extractRoutePath(zkPath);
            ZookeeperCacheManager.ROUTE_MAP.put(routePath, dto);
        });
    }

    @Override
    public void handleDataDeleted(String zkPath) throws Exception {
        String routePath = ZkPathConstants.extractRoutePath(zkPath);
        ZookeeperCacheManager.ROUTE_MAP.remove(routePath);
    }
}
