package com.bird.service.common.grid;

import com.bird.service.common.grid.annotation.AutoGrid;
import com.bird.service.common.grid.executor.DialectType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
@Slf4j
public class GridDefinition {

    /**
     * 表格定义类
     */
    private Class<?> gridClass;
    /**
     * 表格名称
     */
    private String name;
    /**
     * 执行器类型
     */
    private DialectType dialectType;
    /**
     * from ：SQL的from部分
     */
    private String from;
    /**
     * where ：SQL的where部分，不包含前端传递的查询条件
     */
    private String where;
    /**
     * appendSql ：附加的SQL语句
     */
    private String appendSql;
    /**
     * 字段集合
     */
    private Map<String, GridFieldDefinition> fields;

    /**
     * 解析表格类
     *
     * @param gridClass 表格定义类
     * @return 表格描述符
     */
    public static GridDefinition parse(Class<?> gridClass) {
        if (gridClass == null || gridClass.isAnnotationPresent(AutoGrid.class)) {
            return null;
        }

        AutoGrid autoGrid = gridClass.getAnnotation(AutoGrid.class);
        if (StringUtils.isAnyBlank(autoGrid.name(), autoGrid.from())) {
            log.warn("@AutoGrid注解name与from属性为空，忽略表格定义类:{}", gridClass.getName());
            return null;
        }

        GridDefinition gridDefinition = new GridDefinition();
        gridDefinition.setGridClass(gridClass);
        gridDefinition.setName(autoGrid.name());
        gridDefinition.setDialectType(autoGrid.dialectType());
        gridDefinition.setFrom(autoGrid.from());
        gridDefinition.setWhere(autoGrid.where());
        gridDefinition.setAppendSql(autoGrid.appendSql());
        gridDefinition.setFields(parseFields(gridClass));

        if (CollectionUtils.isEmpty(gridDefinition.getFields())) {
            log.warn("忽略表格定义类{}，未定义字段", gridClass.getName());
            return null;
        }

        return gridDefinition;
    }

    private static Map<String, GridFieldDefinition> parseFields(Class<?> gridClass) {
        Map<String, GridFieldDefinition> fieldDefinitions = new HashMap<>(32);

        Field[] fields = gridClass.getDeclaredFields();
        for (Field field : fields) {
            GridFieldDefinition fieldDefinition = GridFieldDefinition.parse(field);
            if (fieldDefinition != null) {
                fieldDefinitions.put(field.getName(), fieldDefinition);
            }
        }
        return fieldDefinitions;
    }
}
