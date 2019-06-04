package com.bird.gateway.common.exception;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public class CommonErrorCode {

    /**
     * 错误 异常码
     */
    public static final int ERROR = 500;

    /**
     * 成功 操作码
     */
    public static final int SUCCESSFUL = 200;

    /**
     * 未登录 异常码
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 未授权 异常码
     */
    public static final int METHOD_NOT_ALLOWED = 405;
}
