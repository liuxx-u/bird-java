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
     * sso服务器地址
     */
    private String server;

    /**
     * 客户端票据缓存时间（单位：分），过期后重新去Server拉取用户信息
     */
    private Integer cache = 30;

    /**
     * 连接sso服务器超时时间
     */
    private Integer ctimeout = 50000;

    /**
     * 读取sso内容超时时间
     */
    private Integer rtimeout = 30000;
}
