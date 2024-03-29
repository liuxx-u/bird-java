package com.bird.service.common.grid;

import com.bird.service.common.grid.annotation.AutoGrid;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.executor.DialectType;
import com.bird.service.common.grid.interceptor.IGridQueryInterceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
@Slf4j
public class GridDefinition {

    private final static String OBJECT_CLASS_NAME = "java.lang.Object";

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
     * 主表，多表关联时用于指定新增、删除数据的表
     */
    private String mainTable;
    /**
     * 主键列
     */
    private String primaryKey;
    /**
     * 逻辑删除字段
     */
    private String logicDeleteField;
    /**
     * where ：SQL的where部分，不包含前端传递的查询条件
     */
    private String where;
    /**
     * appendSql ：附加的SQL语句
     */
    private String appendSql;
    /**
     * 默认的排序字段
     */
    private String defaultSortField;
    /**
     * 默认的排序方式
     */
    private SortDirectionEnum defaultSortDirection;
    /**
     * 字段集合
     */
    private Map<String, GridFieldDefinition> fields;
    /**
     * 查询拦截器类名
     */
    Class<? extends IGridQueryInterceptor> queryInterceptorClass;

    /**
     * 获取主键列定义
     *
     * @return 主键列定义
     */
    public GridFieldDefinition getPrimaryField() {
        return this.getFieldDefinition(this.primaryKey);
    }

    /**
     * 根据字段名获取字段定义
     *
     * @param field 字段名称
     * @return 字段定义
     */
    public GridFieldDefinition getFieldDefinition(String field) {
        if (CollectionUtils.isEmpty(this.fields) || StringUtils.isBlank(field)) {
            return null;
        }
        return this.fields.get(field);
    }

    /**
     * 解析表格类
     *
     * @param gridClass 表格定义类
     * @return 表格描述符
     */
    public static GridDefinition parse(Class<?> gridClass) {
        if (gridClass == null) {
            return null;
        }
        AutoGrid autoGrid = gridClass.getAnnotation(AutoGrid.class);
        if (autoGrid == null) {
            return null;
        }

        if (StringUtils.isAnyBlank(autoGrid.name(), autoGrid.from())) {
            log.warn("@AutoGrid注解name与from属性为空，忽略表格定义类:{}", gridClass.getName());
            return null;
        }

        String logicDeleteField = StringUtils.EMPTY;
        Map<String, GridFieldDefinition> fieldDefinitions = new LinkedHashMap<>(32);

        Class<?> tempClass = gridClass;
        while (tempClass != null && !StringUtils.equals(tempClass.getName(), OBJECT_CLASS_NAME)) {
            Field[] tempFields = tempClass.getDeclaredFields();
            for (Field field : tempFields) {
                GridFieldDefinition fieldDefinition = GridFieldDefinition.parse(field);
                if (fieldDefinition != null) {
                    if (BooleanUtils.isTrue(fieldDefinition.getIsLogicDeleteField())) {
                        logicDeleteField = fieldDefinition.getFieldName();
                    }
                    fieldDefinitions.putIfAbsent(field.getName(), fieldDefinition);
                }
            }
            tempClass = tempClass.getSuperclass();
        }

        if (CollectionUtils.isEmpty(fieldDefinitions)) {
            log.warn("忽略表格定义类{}，未定义字段", gridClass.getName());
            return null;
        }

        GridDefinition gridDefinition = new GridDefinition();
        gridDefinition.setGridClass(gridClass);
        gridDefinition.setName(autoGrid.name());
        gridDefinition.setDialectType(autoGrid.dialectType());
        gridDefinition.setFrom(autoGrid.from());
        gridDefinition.setMainTable(StringUtils.isBlank(autoGrid.mainTable()) ? autoGrid.from() : autoGrid.mainTable());
        gridDefinition.setPrimaryKey(autoGrid.primaryKey());
        gridDefinition.setWhere(autoGrid.where());
        gridDefinition.setAppendSql(autoGrid.appendSql());
        gridDefinition.setDefaultSortField(autoGrid.defaultSortField());
        gridDefinition.setDefaultSortDirection(autoGrid.defaultSortDirection());
        gridDefinition.setFields(fieldDefinitions);
        gridDefinition.setLogicDeleteField(logicDeleteField);
        if (!Objects.equals(autoGrid.queryInterceptorClass(), IGridQueryInterceptor.class)) {
            gridDefinition.setQueryInterceptorClass(autoGrid.queryInterceptorClass());
        }

        return gridDefinition;
    }
}
