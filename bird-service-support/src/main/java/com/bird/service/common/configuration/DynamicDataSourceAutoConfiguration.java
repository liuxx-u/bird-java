package com.bird.service.common.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.bird.service.common.datasource.DynamicDataSource;
import com.bird.service.common.datasource.DynamicDataSourceAspect;
import com.bird.service.common.datasource.IDataSourceCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2020/5/18
 */
@Slf4j
@Configuration
@ConditionalOnClass({Aspect.class, DruidDataSource.class})
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@ConditionalOnProperty(value = "spring.datasource.dynamic.enable", havingValue = "true")
public class DynamicDataSourceAutoConfiguration {

    private IDataSourceCustomizer dataSourceCustomizer;

    public DynamicDataSourceAutoConfiguration(ObjectProvider<IDataSourceCustomizer> dataSourceCustomizerObjectProvider) {
        dataSourceCustomizer = dataSourceCustomizerObjectProvider.getIfAvailable();
    }

    /**
     * 注入动态数据源
     *
     * @param properties           配置信息
     * @return DynamicDataSource
     */
    @Bean
    public DataSource dataSource(DynamicDataSourceProperties properties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        if (MapUtils.isEmpty(properties.getCollection())) {
            return dynamicDataSource;
        }

        if (dataSourceCustomizer != null) {
            properties.getCollection().values().forEach(dataSource -> {
                dataSourceCustomizer.customize(dataSource);
            });
        }

        Map<Object, Object> collection = new HashMap<>(properties.getCollection());
        dynamicDataSource.setDefaultTargetDataSource(collection.get(properties.getDefaultDataSource()));
        dynamicDataSource.setTargetDataSources(collection);
        return dynamicDataSource;
    }

    /**
     * 动态数据源切面
     *
     * @return DynamicDataSourceAspect
     */
    @Bean
    public DynamicDataSourceAspect dataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    /**
     * 设置事务管理器中的数据源为动态数据源
     *
     * @param dataSource dataSource
     * @return PlatformTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
