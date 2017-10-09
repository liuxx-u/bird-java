package com.bird.core.sso;

/**
 * Created by liuxx on 2017/5/18.
 */
public class SsoAuthorizeConfig {
    private String cookieName;
    private String loginPath;
    private TicketInfoProtector protector;

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

    public TicketInfoProtector getProtector() {
        return protector;
    }

    public void setProtector(TicketInfoProtector protector) {
        this.protector = protector;
    }
}
