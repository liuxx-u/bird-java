package com.bird.service.common.grid.configuration;

import com.bird.service.common.grid.AutoGridProperties;
import com.bird.service.common.grid.GridClassContainer;
import com.bird.service.common.grid.controller.GridController;
import com.bird.service.common.grid.executor.DefaultGridExecutorLoader;
import com.bird.service.common.grid.executor.GridExecutorFactory;
import com.bird.service.common.grid.executor.IGridExecutorLoader;
import com.bird.service.common.grid.executor.jdbc.JdbcGridExecutor;
import com.bird.service.common.grid.interceptor.GridInterceptorChain;
import com.bird.service.common.grid.interceptor.IGridInterceptor;
import com.bird.service.common.grid.scanner.IGridDefinitionScanner;
import com.bird.service.common.grid.scanner.SpringGridDefinitionScanner;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @since 2021/2/2
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(GridJdbcAutoConfiguration.class)
@EnableConfigurationProperties(AutoGridProperties.class)
@ConditionalOnProperty(value = "bird.service.grid.base-packages")
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
    @ConditionalOnMissingBean(IGridExecutorLoader.class)
    public IGridExecutorLoader gridExecutorLoader(ObjectProvider<JdbcGridExecutor> jdbcGridExecutor) {
        return new DefaultGridExecutorLoader(jdbcGridExecutor);
    }

    @Bean
    public GridInterceptorChain gridInterceptorChain(ObjectProvider<List<IGridInterceptor>> interceptors) {
        return new GridInterceptorChain(interceptors.getIfAvailable());
    }

    @Bean
    public GridExecutorFactory gridExecutorFactory(GridClassContainer gridClassContainer, GridInterceptorChain interceptorChain, IGridExecutorLoader gridExecutorLoader) {
        return new GridExecutorFactory(gridClassContainer, interceptorChain, gridExecutorLoader);
    }

    @Bean
    public GridController gridController(GridExecutorFactory executorFactory) {
        return new GridController(executorFactory);
    }
}
