package com.bird.gateway.registry.zookeeper;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public final class ZkPathConstant {

    private static final String PLUGIN_PARENT = "/gateway/plugin";

    public static final String ROUTE_PARENT = "/gateway/route";


    private static final String PATH_DELIMITER = "/";
    private static final String ZK_ROUTE_PATH_DELIMITER = "-";

    public static String buildPluginPath(final String pluginName) {
        return buildZkPath(PLUGIN_PARENT, pluginName);
    }

    public static String buildModulePath(final String moduleName) {
        return buildZkPath(ROUTE_PARENT, StringUtils.strip(moduleName, PATH_DELIMITER));
    }

    public static String buildZkRoutePath(final String moduleName, final String routePath) {
        String zkRoutePath = StringUtils.strip(routePath, PATH_DELIMITER).replaceAll("[/]+", ZK_ROUTE_PATH_DELIMITER);

        return buildZkPath(buildModulePath(moduleName), zkRoutePath);
    }

    public static String buildRoutePath(final String moduleName, final String routePath) {
        String path = routePath.replaceAll("[-]+", PATH_DELIMITER);

        return ("/" + moduleName + "/" + path).replaceAll("[/]+", PATH_DELIMITER);
    }

    public static String buildZkPath(String... paths) {
        return String.join(PATH_DELIMITER, paths);
    }

    public static String parseZkRoutePath(String routePath) {
        if (StringUtils.isBlank(routePath)) return null;
        List<String> arr = Arrays.stream(routePath.split("/")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (arr.size() < 2) return null;

        String module = arr.get(0);
        StringBuilder zkRoutePath = new StringBuilder();

        for (int i = 1, len = arr.size(); i < len; i++) {
            zkRoutePath.append(arr.get(i));
            if (i < len - 1) {
                zkRoutePath.append(ZK_ROUTE_PATH_DELIMITER);
            }
        }
        return ROUTE_PARENT + "/" + module + "/" + zkRoutePath;
    }

    public static String extractRoutePath(String zkPath) {
        if (StringUtils.isBlank(zkPath) || !StringUtils.startsWith(zkPath, ROUTE_PARENT)) return null;

        zkPath = StringUtils.stripEnd(zkPath, "/");
        String module = zkPath.substring(ROUTE_PARENT.length(), zkPath.lastIndexOf("/"));
        String routePath = zkPath.substring((ROUTE_PARENT + module).length());
        routePath = routePath.replaceAll("[-]+", "/");

        return (module + routePath).replaceAll("[/]+", "/");
    }
}
