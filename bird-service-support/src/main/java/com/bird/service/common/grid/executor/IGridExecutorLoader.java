package com.bird.service.common.grid.executor;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/2
 */
public interface IGridExecutorLoader {

    /**
     * 加载Grid方法执行器集合
     *
     * @return Grid方法执行器映射Map
     */
    Map<DialectType, IGridExecutor> loadExecutors();
}
