package com.bird.service.common.grid;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bird.service.common.grid.annotation.GridField;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import com.bird.service.common.grid.enums.SaveStrategyEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
     * 是否主键列
     */
    private String isPrimaryKey;
    /**
     * 数据源中字段类型
     */
    private GridFieldType fieldType;
    /**
     * 数据保存策略
     */
    private SaveStrategyEnum saveStrategy;
    /**
     * 字段查询策略
     */
    private QueryStrategyEnum queryStrategy;
    /**
     * 自动填充策略
     */
    private FieldFill fillStrategy;
    /**
     * 是否逻辑删除字段
     */
    private Boolean isLogicDeleteField;
    /**
     * 逻辑删除值
     */
    private Object logicDeleteValue;
    /**
     * 逻辑删除值
     */
    private Object logicNotDeleteValue;

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

        GridFieldDefinition fieldDefinition = new GridFieldDefinition();
        fieldDefinition.setFieldName(field.getName());

        fieldDefinition.setDbField(field.getName());
        fieldDefinition.setFieldType(GridFieldType.NULL);
        fieldDefinition.setSaveStrategy(SaveStrategyEnum.DEFAULT);
        fieldDefinition.setQueryStrategy(QueryStrategyEnum.ALLOW);
        fieldDefinition.setFillStrategy(FieldFill.DEFAULT);
        fieldDefinition.setIsLogicDeleteField(false);

        TableLogic tableLogic = field.getAnnotation(TableLogic.class);
        if(tableLogic != null){
            fieldDefinition.setIsLogicDeleteField(true);
            fieldDefinition.setLogicDeleteValue(tableLogic.delval());
            fieldDefinition.setLogicNotDeleteValue(tableLogic.value());
        }
        fieldDefinition.setIsLogicDeleteField(field.isAnnotationPresent(TableLogic.class));

        GridField gridField = field.getAnnotation(GridField.class);
        if (gridField != null) {
            if (StringUtils.isNotBlank(gridField.dbField())) {
                fieldDefinition.setDbField(gridField.dbField());
            }
            if (!Objects.equals(gridField.fieldType(), GridFieldType.NULL)) {
                fieldDefinition.setFieldType(gridField.fieldType());
            }
            fieldDefinition.setSaveStrategy(gridField.saveStrategy());
            fieldDefinition.setQueryStrategy(gridField.queryStrategy());
        }

        if (Objects.equals(fieldDefinition.fieldType, GridFieldType.NULL)) {
            fieldDefinition.setFieldType(GridFieldType.parse(field.getType().getName()));
        }

        return fieldDefinition;
    }
}
