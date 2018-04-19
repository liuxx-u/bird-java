package com.bird.web.boot.starter.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2018/3/24
 */
@ConfigurationProperties(prefix = SsoConstant.PREFIX)
public class SsoProperties {
    /**
     * cookie名称，默认为sso.token
     */
    private String cookieName = "sso.token";

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
    private Integer expire = 60;

    /**
     * 是否启用SessionStore，默认启用
     */
    private Boolean useSessionStore = true;

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Boolean getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Boolean getUseSessionStore() {
        return useSessionStore;
    }

    public void setUseSessionStore(Boolean useSessionStore) {
        this.useSessionStore = useSessionStore;
    }
}
