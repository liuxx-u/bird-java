package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.pojo.PagedListQuery;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/27
 */
public interface IGridSqlParser {

    /**
     * 解析分页查询SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param query          分页查询参数
     * @return statement
     */
    PreparedStateParameter listPaged(GridDefinition gridDefinition, PagedListQuery query);

    /**
     * 解析分页查询中合计的SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param query          分页查询参数
     * @return statement
     */
    PreparedStateParameter listSum(GridDefinition gridDefinition, PagedListQuery query);

    /**
     * 解析新增SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param pojo           新增数据
     * @return statement
     */
    PreparedStateParameter add(GridDefinition gridDefinition, Map<String, Object> pojo);

    /**
     * 解析更新SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param pojo           新增数据
     * @return statement
     */
    PreparedStateParameter edit(GridDefinition gridDefinition, Map<String, Object> pojo);

    /**
     * 解析删除SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param id             主键
     * @return statement
     */
    PreparedStateParameter delete(GridDefinition gridDefinition, Object id);


    /**
     * 解析逻辑删除SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param id             主键
     * @return statement
     */
    PreparedStateParameter logicDelete(GridDefinition gridDefinition, Object id);
}
