package com.bird.dubbo.gateway.route;

import com.alibaba.fastjson.JSON;
import com.bird.core.utils.AopHelper;
import com.bird.core.utils.SpringContextHolder;
import com.bird.core.utils.StringHelper;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.enums.RpcTypeEnum;
import com.bird.gateway.common.IRouteScanner;
import com.bird.gateway.common.RouteDefinition;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/5/5
 */
public class DubboRouteScanner implements IRouteScanner {

    @Value("${spring.application.name:}")
    private String application;
    @Value("${dubbo.registry.address:}")
    private String registry;
    @Value("${dubbo.protocol.name:dubbo}")
    private String protocol;
    @Value("${dubbo.consumer.loadbalance:leastactive}")
    private String loadbalance;

    @Override
    public List<RouteDefinition> getRoutes(){
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        Map<String,Object> map = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(DubboRoute.class);
        for(Map.Entry<String,Object> entry : map.entrySet()){
            Class<?> clazz = AopHelper.getTarget(entry.getValue()).getClass();
            routeDefinitions.addAll(this.parseRouteDefinition(clazz));
        }
        return routeDefinitions;
    }

    /**
     * 解析网关信息
     *
     * @param clazz clazz
     */
    private List<RouteDefinition> parseRouteDefinition(Class<?> clazz) {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();

        Class<?>[] interfaceClasses = clazz.getInterfaces();
        if (ArrayUtils.isEmpty(interfaceClasses)) return routeDefinitions;

        DubboRoute typeRoute = clazz.getAnnotation(DubboRoute.class);
        if (typeRoute == null) return routeDefinitions;

        Service service = clazz.getAnnotation(Service.class);
        if (service == null) return routeDefinitions;
        String typePath = this.getTypeRoutePath(typeRoute, clazz.getSimpleName());

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            DubboRoute methodRoute = method.getAnnotation(DubboRoute.class);
            if (methodRoute == null) continue;

            String methodPath = this.getMethodRoutePath(methodRoute, method.getName());
            List<String> permissions = new ArrayList<>();
            for (String typePermission : typeRoute.permissions()) {
                for (String methodPermission : methodRoute.permissions()) {
                    permissions.add(typePermission + methodPermission);
                }
            }

            RouteDefinition definition = new RouteDefinition();
            definition.setModule(application);
            definition.setGroup(typePath);
            definition.setPath(typePath + methodPath);
            definition.setDesc(methodRoute.desc());
            definition.setPermissions(StringUtils.join(permissions, ","));
            definition.setCheckAll(methodRoute.isCheckAll());
            definition.setAnonymous(methodRoute.anonymous());
            definition.setEnabled(true);
            definition.setRpcType(RpcTypeEnum.DUBBO.getName());

            DubboHandle handle = new DubboHandle();
            handle.setAppName(this.application);
            handle.setRegistry(this.registry);
            handle.setProtocol(this.protocol);
            handle.setLoadBalance(this.loadbalance);
            handle.setInterfaceName(interfaceClasses[0].getName());
            handle.setMethodName(method.getName());
            handle.setVersion(service.version());
            handle.setTimeout(service.timeout());
            if (handle.getTimeout() <= 0) {
                handle.setTimeout(10000);
            }
            handle.setParameters(JSON.toJSONString(this.getParameterMap(method)));
            definition.setRpcJson(JSON.toJSONString(handle));
            routeDefinitions.add(definition);
        }
        return routeDefinitions;
    }

    /**
     * 获取方法参数名与类型的Map
     *
     * @param method method
     * @return map
     */
    private Map<String, String> getParameterMap(Method method) {
        Map<String, String> map = new LinkedHashMap<>();

        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterNames != null && ArrayUtils.isNotEmpty(parameterTypes) && parameterNames.length == parameterTypes.length) {
            for (int i = 0, len = parameterNames.length; i < len; i++) {
                map.put(parameterNames[i], parameterTypes[i].getName());
            }
        }
        return map;
    }

    /**
     * 获取Type级别的path
     *
     * @param route     注解
     * @param className 类名
     * @return path
     */
    private String getTypeRoutePath(DubboRoute route, String className) {
        if (StringUtils.isNotBlank(route.path())) {
            return formatPath(route.path());
        } else {
            String path = className;
            if (path.endsWith("ServiceImpl")) {
                path = StringUtils.removeEnd(path, "ServiceImpl");
            } else if (path.endsWith("Service")) {
                path = StringUtils.removeEnd(path, "Service");
            }

            path = StringHelper.toCamelCase(path);
            return formatPath(path);
        }
    }

    /**
     * 获取Method级别的path
     *
     * @param route      注解
     * @param methodName 方法名
     * @return path
     */
    private String getMethodRoutePath(DubboRoute route, String methodName) {
        String path = StringUtils.isNotBlank(route.path())
                ? route.path()
                : StringHelper.toCamelCase(methodName);

        return formatPath(path);
    }

    /**
     * 处理path，连续的/去重，确保以/开始
     *
     * @param path path
     * @return /path
     */
    private String formatPath(String path) {
        if (StringUtils.isBlank(path)) return path;

        path = path.replaceAll("[/]+", "/");
        path = StringUtils.removeEnd(path, "/");
        return path.startsWith("/") ? path : "/" + path;
    }
}
