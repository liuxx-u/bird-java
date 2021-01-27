package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.query.PagedListQuery;

import java.sql.Statement;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/27
 */
public interface IGridStatementParser {

    /**
     * 解析列表查询SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param query          分页查询参数
     * @return statement
     */
    Statement listPaged(GridDefinition gridDefinition, PagedListQuery query);

    /**
     * 解析新增SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param pojo           新增数据
     * @return statement
     */
    Statement add(GridDefinition gridDefinition, Map<String, Object> pojo);

    /**
     * 解析更新SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param pojo           新增数据
     * @return statement
     */
    Statement edit(GridDefinition gridDefinition, Map<String, Object> pojo);

    /**
     * 解析删除SQL语句
     *
     * @param gridDefinition 表格描述符
     * @param id             主键
     * @return statement
     */
    Statement delete(GridDefinition gridDefinition, Object id);
}
