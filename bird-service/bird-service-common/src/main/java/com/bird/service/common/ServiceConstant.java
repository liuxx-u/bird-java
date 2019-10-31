package com.bird.service.common;

/**
 * @author liuxx
 * @date 2018/3/8
 *
 * 服务相关的公共常量
 */
public interface ServiceConstant {

    /**
     * 软删除状态
     */
    interface SoftDelete {
        Byte TRUE = 1;
        Byte FALSE = 0;
    }
}
