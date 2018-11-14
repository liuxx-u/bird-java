package com.bird.service.boot.starter.permission;

import com.bird.service.common.mapper.permission.DataRuleInstaller;
import com.bird.service.common.mapper.permission.IDataRuleStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2018/10/10
 */
@Configuration
@ConditionalOnProperty(value = DataRuleConstant.BASE_PACKAGE_PROPERTY_NAME)
@EnableConfigurationProperties(DataRuleProperties.class)
public class DataRuleConfigurer {

    private final DataRuleProperties properties;

    @Autowired
    public DataRuleConfigurer(DataRuleProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean
    public IDataRuleStore dataRuleStore(){
        return new NullDataRuleStore();
    }

    @Bean
    public DataRuleInstaller dataRuleInstaller(){
        return new DataRuleInstaller(properties.getBasePackages());
    }
}
