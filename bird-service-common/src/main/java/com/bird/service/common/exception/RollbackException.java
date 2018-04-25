package com.bird.service.common.exception;

import com.bird.core.exception.AbstractException;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 事务回滚异常类
 */
public class RollbackException extends AbstractException {
    /**
     * 获取异常对应的业务编码
     *
     * @return
     */
    @Override
    public Integer getCode() {
        return 500;
    }
}
