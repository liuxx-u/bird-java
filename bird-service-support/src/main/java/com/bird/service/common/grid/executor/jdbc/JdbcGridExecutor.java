package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.executor.IGridExecutor;
import com.bird.service.common.grid.query.PagedListQuery;
import com.bird.service.common.grid.query.PagedResult;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/20
 */
public class JdbcGridExecutor implements IGridExecutor {

    /**
     * 分页查询
     *
     * @param gridDefinition 表格描述符
     * @param query          分页查询参数
     * @return 查询结果
     */
    @Override
    public PagedResult<Map<String, Object>> listPaged(GridDefinition gridDefinition, PagedListQuery query) {


        return null;
    }

    /**
     * 新增
     *
     * @param gridDefinition 表格描述符
     * @param pojo           新增数据
     * @return 主键
     */
    @Override
    public Object add(GridDefinition gridDefinition, Map<String, Object> pojo) {
        return null;
    }

    /**
     * 编辑
     *
     * @param gridDefinition 表格描述符
     * @param pojo           更新的数据
     * @return 主键
     */
    @Override
    public Object edit(GridDefinition gridDefinition, Map<String, Object> pojo) {
        return null;
    }

    /**
     * 删除
     *
     * @param gridDefinition 表格描述符
     * @param id             主键
     * @return 是否删除成功
     */
    @Override
    public boolean delete(GridDefinition gridDefinition, Object id) {
        return false;
    }
}
