package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.executor.IGridExecutor;
import com.bird.service.common.grid.query.PagedListQuery;
import com.bird.service.common.grid.query.PagedResult;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liuxx
 * @since 2021/1/20
 */
@Slf4j
public class JdbcGridExecutor implements IGridExecutor {

    private final JdbcGridContext jdbcGridContext;

    public JdbcGridExecutor(JdbcGridContext jdbcGridContext) {
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

        PreparedStateParameter sumStateParameter = sqlParser.listSum(gridDefinition, query);
        Map<String, Object> sum = this.execute(sumStateParameter, PreparedStateUtils::readFirstRow);
        if (sum == null) {
            return new PagedResult<>();
        }
        long totalCount = Long.parseLong(sum.getOrDefault("totalCount", 0L).toString());
        if (totalCount <= 0) {
            return new PagedResult<>();
        }

        PreparedStateParameter stateParameter = sqlParser.listPaged(gridDefinition, query);
        List<Map<String, Object>> list = this.execute(stateParameter, PreparedStateUtils::readResultSet);

        return new PagedResult<>(totalCount, list, sum);
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

    /**
     * 执行数据库操作
     *
     * @param stateParameter 执行的SQL及参数
     * @param function       业务逻辑
     * @param <T>            返回的数据类型
     * @return 返回的数据
     */
    private <T> T execute(PreparedStateParameter stateParameter, Function<ResultSet, T> function) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.jdbcGridContext.getConnection();
            statement = PreparedStateUtils.prepareStatement(connection, stateParameter);

            resultSet = statement.executeQuery();
            return function.apply(resultSet);
        } catch (SQLException e) {
            log.error("表格:{}分页查询失败", e);
            return null;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error("ResultSet 关闭失败", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("PreparedStatement 关闭失败", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Connection 关闭失败", e);
                }
            }
        }
    }
}
