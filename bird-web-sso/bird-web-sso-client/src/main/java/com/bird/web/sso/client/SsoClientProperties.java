package com.bird.web.sso.client;

import com.bird.web.sso.SsoProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Getter
@Setter
public class SsoClientProperties extends SsoProperties {

    /**
     * 客户端主机地址
     */
    private String host;

    /**
     * sso服务器地址
     */
    private String server;

    /**
     * 客户端票据缓存时间（单位：分），过期后重新去Server拉取用户信息
     */
    private Integer cache = 30;
}
