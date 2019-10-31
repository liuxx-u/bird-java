package com.bird.service.common.datasource;


import javax.sql.DataSource;

public interface IDataSourceCustomizer {

    /**
     * 自定义数据源处理方法
     * @param dataSource
     */
    void customize(DataSource dataSource);
}
