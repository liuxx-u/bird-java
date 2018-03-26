package com.bird.web.boot.starter.sso;

/**
 * @author liuxx
 * @date 2018/3/24
 */
public interface SsoConstant {
    /**
     * sso 配置前缀
     */
    String PREFIX = "sso";

    /**
     * 单点登录cookie的名称
     */
    String COOKIE_NAME = PREFIX+".cookieName";
}
