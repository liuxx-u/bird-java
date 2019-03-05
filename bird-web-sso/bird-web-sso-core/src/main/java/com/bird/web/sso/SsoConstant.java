package com.bird.web.sso;

/**
 * @author liuxx
 * @date 2019/3/1
 */
public interface SsoConstant {
    /**
     * sso 配置前缀
     */
    String PREFIX = "bird.sso";

    /**
     * sso 服务端配置前缀
     */
    String PREFIX_SERVER = PREFIX + ".server";

    /**
     * sso 服务端Cookie名称
     */
    String SERVER_COOKIE_NAME = PREFIX_SERVER+".cookieName";

    /**
     * sso 服务端是否启用Session Store
     */
    String SERVER_USE_SESSION_STORE = PREFIX_SERVER+".useSessionStore";


    /**
     * sso 客户端配置前缀
     */
    String PREFIX_CLIENT = PREFIX + ".client";

    /**
     * sso 客户端Cookie名称
     */
    String CLIENT_COOKIE_NAME = PREFIX_CLIENT+".cookieName";
}
