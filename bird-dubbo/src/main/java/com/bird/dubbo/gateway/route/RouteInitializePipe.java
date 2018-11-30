package com.bird.dubbo.gateway.route;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.bird.core.initialize.IInitializePipe;
import com.bird.core.utils.StringHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 网关路由信息初始化管道
 *
 * @author liuxx
 * @date 2018/11/15
 */
public class RouteInitializePipe implements IInitializePipe {

    @Value("${dubbo.application.id:}")
    private String application;

    private final IRouteDefinitionRegistry routeRegistry;

    private List<DubboRouteDefinition> routeDefinitions;

    public RouteInitializePipe(IRouteDefinitionRegistry routeRegistry){
        routeDefinitions = new ArrayList<>();
        this.routeRegistry = routeRegistry;
    }

    @Override
    public void scanClass(Class<?> clazz) {
        if(!this.validate())return;

        this.parseRouteDefinition(clazz);
    }

    @Override
    public void initialize() {
        routeRegistry.register(routeDefinitions);
    }

    /**
     * 验证网关初始化参数
     * @return true or false
     */
    private Boolean validate() {
        return routeRegistry != null && StringUtils.isNotBlank(this.application);
    }

    /**
     * 解析网关信息
     * @param clazz clazz
     */
    private void parseRouteDefinition(Class<?> clazz){
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        if(ArrayUtils.isEmpty(interfaceClasses))return;

        DubboRoute typeRoute = clazz.getAnnotation(DubboRoute.class);
        if (typeRoute == null) return;

        Service service = clazz.getAnnotation(Service.class);
        if(service == null)return;

        String crudClass = "";
        if(!StringUtils.equals(typeRoute.crudClass().getName(), Serializable.class.getName())){
            crudClass = typeRoute.crudClass().getName();
        }
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

            DubboRouteDefinition definition = new DubboRouteDefinition();
            definition.setApplication(this.application);
            definition.setInterfaceClass(interfaceClasses[0].getName());
            definition.setMethodName(method.getName());
            definition.setVersion(service.version());
            definition.setParameters(JSON.toJSONString(this.getParameterMap(method)));

            definition.setPath(typePath + methodPath);
            definition.setDesc(methodRoute.desc());
            definition.setCrudClass(crudClass);
            definition.setPermissions(StringUtils.join(permissions,","));
            definition.setIsCheckAll(methodRoute.isCheckAll());
            definition.setAnonymous(methodRoute.anonymous());
            routeDefinitions.add(definition);
        }
    }

    /**
     * 获取方法参数名与类型的Map
     * @param method method
     * @return map
     */
    private Map<String,String> getParameterMap(Method method){
        Map<String,String> map = new LinkedHashMap<>();

        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterNames != null && ArrayUtils.isNotEmpty(parameterTypes) && parameterNames.length == parameterTypes.length){
            for(int i = 0,len = parameterNames.length;i<len;i++){
                map.put(parameterNames[i],parameterTypes[i].getName());
            }
        }
        return map;
    }

    /**
     * 获取Type级别的path
     * @param route 注解
     * @param className 类名
     * @return path
     */
    private String getTypeRoutePath(DubboRoute route,String className) {
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
     * @param route 注解
     * @param methodName 方法名
     * @return path
     */
    private String getMethodRoutePath(DubboRoute route,String methodName) {
        String path = StringUtils.isNotBlank(route.path())
                ? route.path()
                : StringHelper.toCamelCase(methodName);

        return formatPath(path);
    }

    /**
     * 处理path，连续的/去重，确保以/开始
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
