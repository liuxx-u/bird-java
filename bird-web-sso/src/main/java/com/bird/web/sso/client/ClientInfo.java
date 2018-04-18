package com.bird.web.sso.client;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    private String clientName;
    private String host;

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
}
