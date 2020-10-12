package com.bird.web.common.idempotency;

import lombok.Data;

/**
 * @author liuxx
 * @since 2020/8/31
 */
@Data
public class IdempotencyProperties {

    /**
     * 是否开启幂等性校验功能
     */
    private Boolean enabled;

    /**
     * 操作Token有效期，单位分钟
     */
    private Integer expire = 120;
    /**
     * 请求头名称
     */
    private String headerName = "bird-idempotency";
    /**
     * Token失效时提示信息
     */
    private String errorMessage = "该操作已失效，请刷新后重试";
}
