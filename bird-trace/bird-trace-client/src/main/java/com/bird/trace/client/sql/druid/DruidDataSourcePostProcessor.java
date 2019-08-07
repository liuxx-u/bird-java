package com.bird.trace.client.sql.druid;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.sql.SQLException;

/**
 * @author liuxx
 * @date 2019/8/6
 */
@Slf4j
public class DruidDataSourcePostProcessor implements BeanPostProcessor {

    private final static String TRACE_SQL_FILTER_NAME = "traceSQL";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof DruidDataSource) {
            DruidDataSource dataSource = (DruidDataSource) bean;
            try {
                dataSource.addFilters(TRACE_SQL_FILTER_NAME);
            } catch (SQLException e) {
                log.error("add druid trace filter error", e);
            }
        }
        return bean;
    }
}
