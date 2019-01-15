package com.bird.core.aspect.operate;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/1/15
 */
public interface IOperateLogStore {

    /**
     * 存储操作日志信息
     * @param logs 日志集合
     */
    void store(List<OperateLogInfo> logs);
}
