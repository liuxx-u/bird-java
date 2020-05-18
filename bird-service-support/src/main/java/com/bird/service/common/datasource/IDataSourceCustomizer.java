package com.bird.service.common.datasource;


import javax.sql.DataSource;

/**
 * @author liuxx
 * @date 2020/5/18
 */
public interface IDataSourceCustomizer {

    /**
     * 自定义数据源处理方法
     * @param dataSource 数据源
     */
    void customize(DataSource dataSource);
}
