package com.bird.core.exception;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public interface IErrorCode {

    /**
     * 获取错误码
     * @return 错误码
     */
    String getCode();

    /**
     * 获取错误描述
     * @return 错误描述
     */
    String getDesc();
}
