package com.bird.service.common.grid.executor;

import com.bird.service.common.grid.GridClassContainer;
import com.bird.service.common.service.query.PagedListQuery;
import com.bird.service.common.service.query.PagedResult;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/22
 */
public class GridExecutorFactory {

    private final GridClassContainer container;

    public GridExecutorFactory(GridClassContainer container){
        this.container = container;
    }

    /**
     * 列表查询
     *
     * @param gridName 表格名称
     * @param query    查询条件
     * @return 查询结果
     */
    public PagedResult<Map<String, Object>> listPaged(String gridName, PagedListQuery query) {
        return null;
    }

    /**
     * 新增
     *
     * @param gridName 表格名称
     * @param model    实体内容
     * @return 主键id
     */
    public Object insert(String gridName, Map<String, Object> model) {
        return null;
    }

    /**
     * 编辑
     *
     * @param gridName 表格名称
     * @param model    实体内容
     * @return 主键id
     */
    public Object update(String gridName, Map<String, Object> model) {
        return null;
    }

    /**
     * 编辑
     *
     * @param gridName 表格名称
     * @param id       主键id
     */
    public void delete(String gridName, String id) {
    }

    /**
     * 获取表格查询执行器
     * @param gridClass 表格定义类
     * @return 执行器
     */
    private IGridExecutor gridExecutor(Class<?> gridClass){
        return null;
    }
}
