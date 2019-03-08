package com.bird.web.sso;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/3/1
 */
@Getter
@Setter
public abstract class SsoProperties {
    /**
     * cookie名称，默认为Sso-Token
     */
    private String cookieName = "Sso-Token";

    /**
     * 登录地址
     */
    private String loginPath;
}
