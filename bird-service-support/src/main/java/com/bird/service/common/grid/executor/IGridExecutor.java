package com.bird.service.common.grid.executor;

import com.bird.service.common.service.query.PagedListQuery;
import com.bird.service.common.service.query.PagedResult;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/18
 */
public interface IGridExecutor {

    /**
     * 分页查询
     *
     * @param gridClass 表格定义类
     * @param query     分页查询参数
     * @return 查询结果
     */
    PagedResult<Map<String, Object>> listPaged(Class<?> gridClass, PagedListQuery query);

    /**
     * 新增
     *
     * @param gridClass 表格定义类
     * @param pojo      新增数据
     * @return 主键
     */
    Object add(Class<?> gridClass, Map<String, Object> pojo);

    /**
     * 编辑
     *
     * @param gridClass 表格定义类
     * @param pojo      更新的数据
     * @return 主键
     */
    Object edit(Class<?> gridClass, Map<String, Object> pojo);

    /**
     * 删除
     *
     * @param gridClass 表格定义类
     * @param id        主键
     * @return 是否删除成功
     */
    boolean delete(Class<?> gridClass, Object id);
}
