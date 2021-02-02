package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.executor.DialectType;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/1
 */
public class JdbcGridContext {

    private final IGridDataSourceChooser dataSourceChooser;
    private final Map<DialectType, IGridSqlParser> sqlParsers;

    public JdbcGridContext(IGridDataSourceChooser dataSourceChooser, IGridSqlParserLoader sqlParserLoader) {
        this.dataSourceChooser = dataSourceChooser;
        this.sqlParsers = sqlParserLoader.loadSqlParsers();
    }

    /**
     * 根据数据库类型获取SQL解析器
     *
     * @param dialectType 数据库类型
     * @return SQL解析器
     */
    public IGridSqlParser getSqlParser(DialectType dialectType) {
        if (CollectionUtils.isEmpty(this.sqlParsers)) {
            return null;
        }
        return this.sqlParsers.get(dialectType);
    }

    /**
     * 获取执行SQL的数据源
     *
     * @return 数据源
     */
    public DataSource getDataSource() {
        return this.dataSourceChooser.gridDataSource();
    }

    /**
     * 获取执行SQL语句的数据库连接
     *
     * @return 执行SQL语句的数据库连接
     * @throws SQLException SQLException
     */
    public Connection getConnection() throws SQLException {
        DataSource dataSource = this.getDataSource();
        return dataSource.getConnection();
    }
}
