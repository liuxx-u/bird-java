package com.bird.service.common.mapper.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bird.service.common.mapper.QueryDescriptor;
import com.bird.service.common.model.IDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据规则初始化管道
 * @author liuxx
 * @date 2018/10/10
 */
@Slf4j
public class DataRuleInitializer{

    private String basePackages;
    private String applicationName;
    private IDataRuleStore dataRuleStore;

    public DataRuleInitializer(String basePackages,String applicationName,IDataRuleStore dataRuleStore) {
        this.basePackages = basePackages;
        this.applicationName = applicationName;
        this.dataRuleStore = dataRuleStore;
    }


    public void init() {
        //Set<Class<?>> classes = ClassHelper.getClasses(this.basePackages);
        Set<Class<?>> classes = new LinkedHashSet<>();
        Set<DataRuleInfo> ruleInfos = classes.stream()
                .filter(IDO.class::isAssignableFrom)
                .filter(p -> p.isAnnotationPresent(TableName.class))
                .map(this::collectRuleInfos).flatMap(Collection::stream)
                .collect(Collectors.toSet());

        dataRuleStore.store(ruleInfos);
    }

    /**
     * 扫描项目类所有class
     *
     * @param clazz class
     */
    private Set<DataRuleInfo> collectRuleInfos(Class<?> clazz) {
        Set<DataRuleInfo> ruleInfos = new HashSet<>();

        TableName tableName = clazz.getAnnotation(TableName.class);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) {
                continue;
            }

            DataRule dataRule = field.getAnnotation(DataRule.class);

            if (dataRule != null) {
                DataRuleInfo ruleInfo = new DataRuleInfo();
                ruleInfo.setAppName(applicationName);
                ruleInfo.setClassName(clazz.getName());
                ruleInfo.setFieldName(field.getName());
                ruleInfo.setDbFieldName(QueryDescriptor.getDbFieldName(field));
                ruleInfo.setName(dataRule.name());
                ruleInfo.setSourceStrategy(String.valueOf(dataRule.strategy().getKey()));
                ruleInfo.setTableName(StringUtils.wrapIfMissing(tableName.value(), "`"));
                if (RuleSourceStrategy.CHOICE.equals(dataRule.strategy())) {
                    ruleInfo.setSourceUrl(dataRule.url());
                }
                if (RuleSourceStrategy.SYSTEM.equals(dataRule.strategy())) {
                    ruleInfo.setSourceProvider(dataRule.provider().getName());
                }

                ruleInfos.add(ruleInfo);
            }
        }
        return ruleInfos;
    }
}
