package com.bird.core.sso.client;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    private String clientName;
    private String host;
    private String loginNotifyUrl;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLoginNotifyUrl() {
        return loginNotifyUrl;
    }

    public void setLoginNotifyUrl(String loginNotifyUrl) {
        this.loginNotifyUrl = loginNotifyUrl;
    }
}
