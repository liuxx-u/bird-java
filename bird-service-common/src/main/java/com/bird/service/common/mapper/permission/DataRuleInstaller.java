package com.bird.service.common.mapper.permission;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.Check;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.SpringContextHolder;
import com.bird.service.common.mapper.QueryDescriptor;
import com.bird.service.common.model.IModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liuxx
 * @date 2018/10/10
 */
public class DataRuleInstaller {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String packageName;

    public DataRuleInstaller(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 初始化数据规则元
     */
    @PostConstruct
    public void initialize() {
        Check.NotNull(packageName, "packageName");

        Set<Class<?>> classes = ClassHelper.getClasses(packageName);
        if (CollectionUtils.isEmpty(classes)) {
            logger.warn(packageName + "路径下扫描的实体不存在");
            return;
        }
        Set<DataRuleInfo> ruleInfos = new HashSet<>();
        Set<Class<?>> models = ClassHelper.getByInterface(IModel.class, classes);
        for (Class<?> modelClass : models) {
            TableName tableName = modelClass.getAnnotation(TableName.class);
            if (tableName == null) {
                logger.warn(modelClass.getName() + "模型未标注@TableName注解");
                continue;
            }
            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) continue;

                DataRule dataRule = field.getAnnotation(DataRule.class);

                if (dataRule != null) {
                    DataRuleInfo ruleInfo = new DataRuleInfo();
                    ruleInfo.setAppName(SpringContextHolder.getBean(Environment.class).getProperty("spring.application.name"));
                    ruleInfo.setClassName(modelClass.getName());
                    ruleInfo.setFieldName(field.getName());
                    ruleInfo.setDbFieldName(QueryDescriptor.getDbFieldName(field));
                    ruleInfo.setName(dataRule.name());
                    ruleInfo.setSourceStrategy(String.valueOf(dataRule.strategy().getKey()));
                    ruleInfo.setTableName(StringUtils.wrapIfMissing(tableName.value(),"`"));
                    if(RuleSourceStrategy.CHOICE.equals(dataRule.strategy())){
                        ruleInfo.setSourceUrl(dataRule.url());
                    }
                    if(RuleSourceStrategy.SYSTEM.equals(dataRule.strategy())){
                        ruleInfo.setSourceProvider(dataRule.provider().getName());
                    }

                    ruleInfos.add(ruleInfo);
                }
            }
        }
        try {
            IDataRuleStore store = SpringContextHolder.getBean(IDataRuleStore.class);
            store.store(ruleInfos);
        } catch (Exception ex) {
            logger.error("数据规则初始化失败，msg：" + ex.getMessage(), ex);
        }
    }
}
