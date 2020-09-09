package com.bird.web.common.cors;

import lombok.Data;

/**
 * @author liuxx
 * @since 2020/9/2
 */
@Data
public class CorsProperties {

    /**
     * 是否开启跨域资源共享功能
     */
    private Boolean enable;

    private String urlPatterns = "/*";

    private String allowOrigin = "default";

    private String allowMethods = "POST, GET, OPTIONS, DELETE";

    private String allowHeaders = "Origin,X-Requested-With,Content-Type,Accept,Sso-Token,bird-idempotency,appId,tenantId,JSESSIONID";

    private String maxAge = "3600";

    private String allowCredentials = "false";
}
