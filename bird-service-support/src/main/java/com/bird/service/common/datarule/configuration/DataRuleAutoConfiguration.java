package com.bird.service.common.datarule.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.bird.service.common.datarule.DataRuleInitializer;
import com.bird.service.common.datarule.IDataRuleStore;
import com.bird.service.common.datarule.NullDataRuleStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@ConditionalOnProperty(value = "bird.service.data-rule.enabled",havingValue = "true")
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class DataRuleAutoConfiguration {

    @Value("${spring.application.name:}")
    private String applicationName;

    private final MybatisPlusProperties properties;

    @Autowired
    public DataRuleAutoConfiguration(MybatisPlusProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public IDataRuleStore dataRuleStore() {
        return new NullDataRuleStore();
    }

    @Bean
    public DataRuleInitializer dataRuleInitializePipe(IDataRuleStore dataRuleStore) {
        DataRuleInitializer dataRuleInitializer = new DataRuleInitializer(properties.getTypeAliasesPackage(), applicationName, dataRuleStore);
        dataRuleInitializer.init();

        return dataRuleInitializer;
    }
}
