package com.bird.gateway.web.zookeeper;

import com.bird.gateway.common.constant.ZkPathConstants;
import com.bird.gateway.common.dto.zk.ModuleZkDTO;
import com.bird.gateway.common.dto.zk.PluginZkDTO;
import com.bird.gateway.common.route.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Slf4j
@SuppressWarnings("all")
public class ZookeeperCacheManager implements CommandLineRunner, DisposableBean {

    private static final Map<String, PluginZkDTO> PLUGIN_MAP = Maps.newConcurrentMap();

    private static final Map<String, ModuleZkDTO> MODULE_MAP = Maps.newConcurrentMap();
    static final Map<String, RouteDefinition> ROUTE_MAP = Maps.newConcurrentMap();

    private final ZkClient zkClient;

    @Value("${bird.gatewey.upstream.time:30}")
    private int upstreamTime;

    private final ZkRouteDataListener zkRouteDataListener;

    public ZookeeperCacheManager(final ZkClient zkClient, ZkRouteDataListener zkRouteDataListener) {
        this.zkClient = zkClient;
        this.zkRouteDataListener = zkRouteDataListener;
    }

    public static RouteDefinition getRouteDefinition(String path) {
        return ROUTE_MAP.get(path);
    }

    @Override
    public void run(String... args) throws Exception {
        this.loadPlugin();
        this.loadAllRoute();
    }

    @Override
    public void destroy() throws Exception {
        zkClient.close();
    }

    private void loadPlugin() {
        Arrays.stream(PipeEnum.values()).forEach(pluginEnum -> {
            String pluginPath = ZkPathConstants.buildPluginPath(pluginEnum.getName());
            if (!zkClient.exists(pluginPath)) {
                zkClient.createPersistent(pluginPath, true);
            }
            PluginZkDTO data = zkClient.readData(pluginPath);
            Optional.ofNullable(data).ifPresent(d -> PLUGIN_MAP.put(pluginEnum.getName(), data));
            zkClient.subscribeDataChanges(pluginPath, new IZkDataListener() {
                @Override
                public void handleDataChange(final String dataPath, final Object data) {
                    Optional.ofNullable(data).ifPresent(o -> {
                        PluginZkDTO dto = (PluginZkDTO) o;
                        PLUGIN_MAP.put(dto.getName(), dto);
                    });
                }

                @Override
                public void handleDataDeleted(final String dataPath) {
                    PLUGIN_MAP.remove(pluginEnum.getName());
                }
            });
        });
    }

    /**
     * 从zookeeper中加载模块以及路由信息
     */
    private void loadAllRoute() {
        if (!zkClient.exists(ZkPathConstants.ROUTE_PARENT)) {
            zkClient.createPersistent(ZkPathConstants.ROUTE_PARENT, true);
        }

        List<String> modules = zkClient.getChildren(ZkPathConstants.ROUTE_PARENT);
        modules.forEach(this::loadWatchModule);

        zkClient.subscribeChildChanges(ZkPathConstants.ROUTE_PARENT, (parentPath, currentModules) -> {
            List<String> existModules = new ArrayList<>(MODULE_MAP.keySet());

            List<String> newModules = ListUtils.subtract(currentModules, existModules);
            List<String> removeModules = ListUtils.subtract(existModules, currentModules);

            newModules.forEach(this::loadWatchModule);
            removeModules.forEach(this::unloadModule);
        });
    }

    /**
     * 加载并监听路由模块
     *
     * @param module module
     */
    private void loadWatchModule(String module) {
        String modulePath = ZkPathConstants.buildModulePath(module);
        ModuleZkDTO data = zkClient.readData(modulePath);
        if (data == null) {
            data = new ModuleZkDTO();
            data.setPath(module);
        }
        if (!StringUtils.equals(module, data.getPath())) {
            log.warn("module path[" + data.getPath() + "] does not match zookeeper node path.");
        }

        zkClient.subscribeChildChanges(modulePath, (parentPath, children) -> {
            List<String> existPaths = ROUTE_MAP.keySet().stream()
                    .filter(key -> StringUtils.equals(ROUTE_MAP.get(key).getModule(), module))
                    .collect(Collectors.toList());
            List<String> currentPaths = children == null
                    ? new ArrayList<>()
                    : children.stream().map(p -> ZkPathConstants.buildRoutePath(module, p)).collect(Collectors.toList());

            List<String> newPaths = ListUtils.subtract(currentPaths, existPaths);
            List<String> removePaths = ListUtils.subtract(existPaths, currentPaths);

            newPaths.forEach(p -> {
                String zkPath = ZkPathConstants.parseZkRoutePath(p);
                this.loadWatchRoute(zkPath);
            });
            removePaths.forEach(this::unloadRoute);
        });
        this.expandModules(data);
        this.loadModuleRoute(modulePath);
    }

    /**
     * 卸载 路由模块
     *
     * @param module module
     */
    private void unloadModule(String module) {
        MODULE_MAP.remove(module);

        String zkPath = ZkPathConstants.buildModulePath(module);
        zkClient.delete(zkPath);
    }


    /**
     * 展开模块及其子模块
     *
     * @param module module
     */
    private void expandModules(ModuleZkDTO module) {
        if (module == null) return;

        MODULE_MAP.put(module.getPath(), module);
        if (!CollectionUtils.isEmpty(module.getChildren())) {
            for (ModuleZkDTO child : module.getChildren()) {
                expandModules(child);
            }
        }
    }

    /**
     * 从zookeeper加载模块下的路由信息
     *
     * @param zkModulePath zkModulePath
     */
    private void loadModuleRoute(String zkModulePath) {
        List<String> routes = zkClient.getChildren(zkModulePath);
        routes.forEach(route -> {
            String zkRoutePath = ZkPathConstants.buildZkPath(zkModulePath, route);
            this.loadWatchRoute(zkRoutePath);
        });
    }

    /**
     * 加载并监听路由信息
     *
     * @param zkRoutePath 路由节点
     */
    private void loadWatchRoute(String zkRoutePath) {
        RouteDefinition routeDefinition = zkClient.readData(zkRoutePath);
        Optional.ofNullable(routeDefinition).ifPresent(d -> {
            String path = ZkPathConstants.extractRoutePath(zkRoutePath);
            ROUTE_MAP.put(path, d);
        });

        zkClient.subscribeDataChanges(zkRoutePath, zkRouteDataListener);
    }

    /**
     * 卸载路由
     *
     * @param path 路径
     */
    private void unloadRoute(String path) {
        ROUTE_MAP.remove(path);

        String zkPath = ZkPathConstants.parseZkRoutePath(path);
        zkClient.unsubscribeDataChanges(zkPath, zkRouteDataListener);
        zkClient.delete(zkPath);
    }
}
