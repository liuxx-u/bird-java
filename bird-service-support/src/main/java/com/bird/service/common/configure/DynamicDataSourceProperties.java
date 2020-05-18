package com.bird.service.common.configure;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author liuxx
 * @date 2020/5/18
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {
    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 数据源集合
     */
    private Map<String, DruidDataSource> collection;

    /**
     * 默认的数据源
     */
    private String defaultDataSource = "default";
}
