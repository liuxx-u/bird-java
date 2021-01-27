package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.query.PagedListQuery;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;

/**
 * @author liuxx
 * @since 2021/1/20
 */
public abstract class AbstractGridStatementParser implements IGridStatementParser {

    private final IGridDataSourceChooser dataSourceChooser;

    public AbstractGridStatementParser(IGridDataSourceChooser dataSourceChooser) {
        this.dataSourceChooser = dataSourceChooser;
    }

    private Connection connection() {
        DataSource dataSource = this.dataSourceChooser.gridDataSource();
        if (dataSource == null) {
            throw new RuntimeException("dataSource is null");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

    @Override
    public Statement listPaged(GridDefinition gridDefinition, PagedListQuery query) {
//        connection().prepareStatement("").set; SQLType
        return null;
    }
}
