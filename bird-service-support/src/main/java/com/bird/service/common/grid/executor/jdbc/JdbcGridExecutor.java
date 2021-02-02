package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.executor.IGridExecutor;
import com.bird.service.common.grid.query.PagedListQuery;
import com.bird.service.common.grid.query.PagedListResult;
import com.bird.service.common.grid.query.PagedResult;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/20
 */
@Slf4j
public class JdbcGridExecutor implements IGridExecutor {

    private final JdbcGridContext jdbcGridContext;

    public JdbcGridExecutor(JdbcGridContext jdbcGridContext){
        this.jdbcGridContext = jdbcGridContext;
    }

    /**
     * 分页查询
     *
     * @param gridDefinition 表格描述符
     * @param query          分页查询参数
     * @return 查询结果
     */
    @Override
    public PagedResult<Map<String, Object>> listPaged(GridDefinition gridDefinition, PagedListQuery query) {
        IGridSqlParser sqlParser = this.jdbcGridContext.getSqlParser(gridDefinition.getDialectType());
        if (sqlParser == null) {
            log.warn("表格:{}指定的数据源类型:{} 未设置SQL解析器", gridDefinition.getName(), gridDefinition.getDialectType());
            return new PagedResult<>();
        }

        PreparedStateParameter stateParameter = sqlParser.listPaged(gridDefinition, query);

        try {
            Connection connection = this.jdbcGridContext.getConnection();
            PreparedStatement statement = PreparedStateUtils.prepareStatement(connection, stateParameter);

            ResultSet resultSet = statement.executeQuery();
            List<Map<String,Object>> list = PreparedStateUtils.readResultSet(resultSet);
            return new PagedResult<>(10L, list);

        } catch (SQLException e) {
            log.error("表格:{}分页查询失败", e);
            return new PagedResult<>();
        }
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
