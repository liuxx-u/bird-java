package com.bird.gateway.web.zookeeper;

import com.bird.gateway.common.constant.ZkPathConstants;
import com.bird.gateway.common.dto.zk.ModuleZkDTO;
import com.bird.gateway.common.dto.zk.PluginZkDTO;
import com.bird.gateway.common.dto.zk.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Slf4j
@SuppressWarnings("all")
public class ZookeeperCacheManager implements CommandLineRunner, DisposableBean {

    static final Map<String, PluginZkDTO> PLUGIN_MAP = Maps.newConcurrentMap();

    static final Map<String, ModuleZkDTO> MODULE_MAP = Maps.newConcurrentMap();
    static final Map<String, RouteDefinition> ROUTE_MAP = Maps.newConcurrentMap();

    final ZkClient zkClient;

    @Value("${bird.gatewey.upstream.time:30}")
    private int upstreamTime;

    public ZookeeperCacheManager(final ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public static RouteDefinition getRouteDefinition(String path){
        return ROUTE_MAP.get(path);
    }

    @Override
    public void run(String... args) throws Exception {
        this.loadPlugin();
        this.loadModuleAndRoute();
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
    private void loadModuleAndRoute() {
        if (!zkClient.exists(ZkPathConstants.ROUTE_PARENT)) {
            zkClient.createPersistent(ZkPathConstants.ROUTE_PARENT, true);
        }

        List<String> modules = zkClient.getChildren(ZkPathConstants.ROUTE_PARENT);
        modules.stream().forEach(module -> {
            String modulePath = ZkPathConstants.buildModulePath(module);
            ModuleZkDTO data = zkClient.readData(modulePath);
            Optional.ofNullable(data).ifPresent(d -> {
                if (!StringUtils.equals(module, d.getPath())) {
                    log.warn("module path[" + d.getPath() + "] does not match zookeeper node path.");
                } else {
                    this.expandModules(d);
                    this.loadModuleRoute(modulePath);
                }
            });
        });
    }

    /**
     * 展开模块及其子模块
     * @param module module
     */
    private void expandModules(ModuleZkDTO module){
        if(module == null)return;

        MODULE_MAP.put(module.getPath(),module);
        if(!CollectionUtils.isEmpty(module.getChildren())){
            for(ModuleZkDTO child : module.getChildren()){
                expandModules(child);
            }
        }
    }

    /**
     * 从zookeeper加载模块下的路由信息
     * @param zkModulePath zkModulePath
     */
    private void loadModuleRoute(String zkModulePath){
        List<String> routes = zkClient.getChildren(zkModulePath);
        routes.stream().forEach(route->{
            String zkRoutePath = ZkPathConstants.buildZkPath(zkModulePath,route);
            RouteDefinition routeDefinition = zkClient.readData(zkRoutePath);
            Optional.ofNullable(routeDefinition).ifPresent(d->{
                String path = ZkPathConstants.formatZkRoutePath(zkRoutePath);
                path = path.substring(ZkPathConstants.ROUTE_PARENT.length(),path.length());
                ROUTE_MAP.put(path,d);
            });
        });
    }
}
