package com.bird.service.common.grid.executor.jdbc;

import javax.sql.DataSource;

/**
 * @author liuxx
 * @since 2021/1/20
 */
public class DefaultGridDataSourceChooser implements IDataSourceChooser {

    private final DataSource dataSource;

    public DefaultGridDataSourceChooser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 表格操作的数据源
     *
     * @return 数据源
     */
    @Override
    public DataSource gridDataSource() {
        return this.dataSource;
    }
}
