package com.bird.web.boot.starter.sso;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2018/3/24
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SsoConstant.PREFIX)
public class SsoProperties {
    /**
     * cookie名称，默认为Sso-Token
     */
    private String cookieName = "Sso-Token";

    /**
     * 登录地址
     */
    private String loginPath;

    /**
     * 是否自动刷新token，只在启用SessionStore后有效
     */
    private Boolean autoRefresh = true;

    /**
     * 过期时间（单位：分）
     */
    private Integer expire = 24 * 60;

    /**
     * 是否启用SessionStore，默认启用
     */
    private Boolean useSessionStore = true;
}
