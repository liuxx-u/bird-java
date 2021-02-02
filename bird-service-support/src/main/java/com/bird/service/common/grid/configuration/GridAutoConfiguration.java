package com.bird.service.common.grid.configuration;

import com.bird.service.common.grid.AutoGridProperties;
import com.bird.service.common.grid.GridClassContainer;
import com.bird.service.common.grid.controller.GridController;
import com.bird.service.common.grid.executor.DefaultGridExecutorLoader;
import com.bird.service.common.grid.executor.GridExecutorFactory;
import com.bird.service.common.grid.executor.IGridExecutorLoader;
import com.bird.service.common.grid.executor.jdbc.*;
import com.bird.service.common.grid.scanner.IGridDefinitionScanner;
import com.bird.service.common.grid.scanner.SpringGridDefinitionScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author liuxx
 * @since 2021/2/2
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "bird.service.grid.base-packages")
@EnableConfigurationProperties(AutoGridProperties.class)
public class GridAutoConfiguration {

    private final AutoGridProperties gridProperties;

    public GridAutoConfiguration(AutoGridProperties gridProperties) {
        this.gridProperties = gridProperties;
    }

    @Bean
    @ConditionalOnMissingBean(IGridDefinitionScanner.class)
    public IGridDefinitionScanner gridDefinitionScanner() {
        return new SpringGridDefinitionScanner();
    }

    @Bean
    public GridClassContainer gridClassContainer(IGridDefinitionScanner gridDefinitionScanner) {
        return new GridClassContainer(this.gridProperties, gridDefinitionScanner);
    }

    @Bean
    @ConditionalOnMissingBean(IGridDataSourceChooser.class)
    public IGridDataSourceChooser gridDataSourceChooser(DataSource dataSource) {
        return new DefaultGridDataSourceChooser(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(IGridSqlParserLoader.class)
    public IGridSqlParserLoader gridSqlParserLoader() {
        return new DefaultGridSqlParserLoader();
    }

    @Bean
    public JdbcGridContext jdbcGridContext(IGridDataSourceChooser gridDataSourceChooser, IGridSqlParserLoader gridSqlParserLoader) {
        return new JdbcGridContext(gridDataSourceChooser, gridSqlParserLoader);
    }

    @Bean
    public JdbcGridExecutor jdbcGridExecutor(JdbcGridContext jdbcGridContext) {
        return new JdbcGridExecutor(jdbcGridContext);
    }

    @Bean
    @ConditionalOnMissingBean(IGridExecutorLoader.class)
    public IGridExecutorLoader gridExecutorLoader(ObjectProvider<JdbcGridExecutor> jdbcGridExecutor) {
        return new DefaultGridExecutorLoader(jdbcGridExecutor);
    }

    @Bean
    public GridExecutorFactory gridExecutorFactory(GridClassContainer gridClassContainer, IGridExecutorLoader gridExecutorLoader) {
        return new GridExecutorFactory(gridClassContainer, gridExecutorLoader);
    }

    @Bean
    public GridController gridController(GridExecutorFactory executorFactory) {
        return new GridController(executorFactory);
    }
}
