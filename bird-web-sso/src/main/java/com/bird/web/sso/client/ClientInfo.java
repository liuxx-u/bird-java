package com.bird.web.sso.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxx
 */
@Data
public class ClientInfo implements Serializable {
    private String clientName;
    private String host;
}
