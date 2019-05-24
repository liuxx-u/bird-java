package com.bird.web.sso.client;

import com.bird.web.sso.SsoConstant;
import com.bird.web.sso.SsoProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SsoConstant.PREFIX_CLIENT)
public class SsoClientProperties extends SsoProperties {

    /**
     * 是否是webflux环境
     */
    private Boolean webflux;

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
