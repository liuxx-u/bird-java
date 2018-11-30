package com.bird.gateway.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public final class ZkPathConstants implements Constants {

    private static final String PLUGIN_PARENT = "/gateway/plugin";

    public static final String ROUTE_PARENT = "/gateway/route";


    private static final String PATH_DELIMITER = "/";
    public static final String ZK_ROUTE_PATH_DELIMITER = "-";

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

    public static String buildZkPath(String... paths) {
        return String.join(PATH_DELIMITER, paths);
    }

    public static String formatZkRoutePath(final String zkPath) {
        return StringUtils.replaceAll(zkPath, ZK_ROUTE_PATH_DELIMITER, PATH_DELIMITER);
    }
}
