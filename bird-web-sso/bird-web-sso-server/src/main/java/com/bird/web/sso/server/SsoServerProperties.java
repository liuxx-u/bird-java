package com.bird.web.sso.server;

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
@ConfigurationProperties(prefix = SsoConstant.PREFIX_SERVER)
public class SsoServerProperties extends SsoProperties {

    /**
     * 是否自动刷新token，只在启用SessionStore后有效
     */
    private Boolean autoRefresh = true;

    /**
     * 过期时间（单位：分）
     */
    private Integer expire = 120;

    /**
     * 是否启用SessionStore，默认启用
     */
    private Boolean useSessionStore = true;
}
