package com.bird.service.common.grid;

import com.bird.service.common.grid.annotation.GridField;
import com.bird.service.common.grid.enums.SaveStrategyEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
public class GridFieldDefinition {

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 数据源对应的字段名
     */
    private String dbField;
    /**
     * 数据源中字段类型
     */
    private GridFieldType fieldType;
    /**
     * 数据保存策略
     */
    private SaveStrategyEnum saveStrategy;


    /**
     * 排除的列
     */
    private static final List<String> EXCLUDE_FIELD_NAMES = Collections.singletonList("serialVersionUID");

    /**
     * 解析表格字段
     *
     * @param field 字段名
     * @return 表格字段描述符
     */
    static GridFieldDefinition parse(Field field) {
        if (field == null || EXCLUDE_FIELD_NAMES.contains(field.getName())) {
            return null;
        }

        GridFieldDefinition fieldDescriptor = new GridFieldDefinition();
        fieldDescriptor.setFieldName(field.getName());

        GridField gridField = field.getAnnotation(GridField.class);
        if (gridField == null) {
            fieldDescriptor.setDbField(field.getName());
            fieldDescriptor.setSaveStrategy(SaveStrategyEnum.DEFAULT);
        } else {
            fieldDescriptor.setDbField(gridField.dbField());
            fieldDescriptor.setFieldType(gridField.fieldType());
            fieldDescriptor.setSaveStrategy(gridField.saveStrategy());
        }

        if (Objects.equals(fieldDescriptor.fieldType, GridFieldType.NULL)) {
            fieldDescriptor.setFieldType(GridFieldType.parse(field.getType().getName()));
        }

        return fieldDescriptor;
    }
}
