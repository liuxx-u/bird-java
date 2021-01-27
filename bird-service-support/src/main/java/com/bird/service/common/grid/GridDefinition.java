package com.bird.service.common.grid;

import com.bird.service.common.grid.annotation.AutoGrid;
import com.bird.service.common.grid.executor.DialectType;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
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
    private List<GridFieldDefinition> fields;

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
        GridDefinition gridDefinition = new GridDefinition();
        gridDefinition.setGridClass(gridClass);
        gridDefinition.setName(autoGrid.name());
        gridDefinition.setDialectType(autoGrid.dialectType());
        gridDefinition.setFrom(autoGrid.from());
        gridDefinition.setWhere(autoGrid.where());
        gridDefinition.setAppendSql(autoGrid.appendSql());
        gridDefinition.setFields(parseFields(gridClass));

        return gridDefinition;
    }

    private static List<GridFieldDefinition> parseFields(Class<?> gridClass) {
        List<GridFieldDefinition> fieldDescriptors = new ArrayList<>();

        Field[] fields = gridClass.getDeclaredFields();
        for (Field field : fields) {
            GridFieldDefinition fieldDescriptor = GridFieldDefinition.parse(field);
            if (fieldDescriptor != null) {
                fieldDescriptors.add(fieldDescriptor);
            }
        }
        return fieldDescriptors;
    }
}
