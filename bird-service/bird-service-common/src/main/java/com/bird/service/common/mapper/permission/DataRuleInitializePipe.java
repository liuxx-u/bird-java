package com.bird.service.common.mapper.permission;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.Check;
import com.bird.core.initialize.IInitializePipe;
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
 * 数据规则初始化管道
 * @author liuxx
 * @date 2018/10/10
 */
public class DataRuleInitializePipe implements IInitializePipe {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String packageName;
    private Set<DataRuleInfo> ruleInfos;

    public DataRuleInitializePipe(String packageName) {
        this.packageName = packageName;
        ruleInfos = new HashSet<>();
    }

    /**
     * 扫描项目类所有class
     *
     * @param clazz class
     */
    @Override
    public void scanClass(Class<?> clazz) {
        if (StringUtils.isBlank(this.packageName)) return;
        if (!IModel.class.isAssignableFrom(clazz) || !StringUtils.startsWith(clazz.getPackage().getName(), this.packageName))
            return;

        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            logger.warn(clazz.getName() + "模型未标注@TableName注解");
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;

            DataRule dataRule = field.getAnnotation(DataRule.class);

            if (dataRule != null) {
                DataRuleInfo ruleInfo = new DataRuleInfo();
                ruleInfo.setAppName(SpringContextHolder.getBean(Environment.class).getProperty("spring.application.name"));
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
    }

    /**
     * 初始化数据规则元
     */
    @Override
    public void initialize() {
        if (CollectionUtils.isEmpty(ruleInfos)) return;
        try {
            IDataRuleStore store = SpringContextHolder.getBean(IDataRuleStore.class);
            store.store(ruleInfos);
        } catch (Exception ex) {
            logger.error("数据规则初始化失败，msg：" + ex.getMessage(), ex);
        }
    }
}
