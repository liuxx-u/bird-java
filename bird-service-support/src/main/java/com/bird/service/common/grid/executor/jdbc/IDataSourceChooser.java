package com.bird.service.common.grid.executor.jdbc;

import javax.sql.DataSource;

/**
 * @author liuxx
 * @since 2021/1/20
 */
public interface IDataSourceChooser {

    /**
     * 表格操作的数据源
     *
     * @return 数据源
     */
    DataSource gridDataSource();
}
