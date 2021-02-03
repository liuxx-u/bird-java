package com.bird.service.common.grid.configuration;

import com.bird.service.common.grid.executor.jdbc.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(AutoGridJdbcProperties.class)
public class GridJdbcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IGridDataSourceChooser.class)
    public IGridDataSourceChooser gridDataSourceChooser(ObjectProvider<DataSource> dataSource) {
        return new DefaultGridDataSourceChooser(dataSource.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(IGridSqlParserLoader.class)
    public IGridSqlParserLoader gridSqlParserLoader(AutoGridJdbcProperties gridJdbcProperties) {
        return new DefaultGridSqlParserLoader(gridJdbcProperties);
    }

    @Bean
    public JdbcGridContext jdbcGridContext(IGridDataSourceChooser gridDataSourceChooser, IGridSqlParserLoader gridSqlParserLoader) {
        return new JdbcGridContext(gridDataSourceChooser, gridSqlParserLoader);
    }

    @Bean
    public JdbcGridExecutor jdbcGridExecutor(JdbcGridContext jdbcGridContext) {
        return new JdbcGridExecutor(jdbcGridContext);
    }
}
