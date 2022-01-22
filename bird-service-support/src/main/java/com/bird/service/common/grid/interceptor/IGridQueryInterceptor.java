package com.bird.service.common.grid.interceptor;

import com.bird.service.common.grid.pojo.PagedListQuery;
import com.bird.service.common.grid.pojo.PagedResult;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/7
 */
public interface IGridQueryInterceptor {

    /**
     * 查询前置处理
     *
     * @param pagedQuery 查询条件
     */
    default void preQuery(PagedListQuery pagedQuery) {
    }

    /**
     * 查询后置处理
     *
     * @param pagedResult 查询结果
     */
    default void afterQuery(PagedResult<Map<String, Object>> pagedResult) {
    }
}
