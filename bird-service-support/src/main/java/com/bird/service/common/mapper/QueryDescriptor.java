package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bird.service.common.service.query.FilterGroup;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/9/28
 */
public class QueryDescriptor implements Serializable {

    /**
     * 排除的列
     */
    private static final List<String> EXCLUDE_FIELD_NAMES = Collections.singletonList("serialVersionUID");
    private final static String OBJECT_CLASS_NAME = "java.lang.Object";

    private String select;
    private String from;
    private Map<String, String> fieldMap;

    private QueryDescriptor(String select, String from, Map<String, String> fieldMap) {
        this.select = select;
        this.from = from;
        this.fieldMap = fieldMap;
    }

    public static QueryDescriptor parseClass(Class<?> tClass) {
        Map<String, String> fieldMap = new HashMap<>(16);

        if (tClass == null) return null;
        Class tempClass = tClass;
        StringBuilder sb = new StringBuilder();

        while (tempClass != null && !StringUtils.equals(tempClass.getName(), OBJECT_CLASS_NAME)) {
            Field[] tempFields = tempClass.getDeclaredFields();
            for (Field field : tempFields) {
                if(EXCLUDE_FIELD_NAMES.contains(field.getName()))continue;

                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) continue;

                String fieldName = StringUtils.wrapIfMissing(field.getName(), '`');
                String dbFieldName = getDbFieldName(field);
                fieldMap.putIfAbsent(fieldName, dbFieldName);
            }

            tempClass = tempClass.getSuperclass();
        }

        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            sb.append(entry.getValue());
            sb.append(" AS ");
            sb.append(entry.getKey());
            sb.append(",");
        }

        String select = StringUtils.stripEnd(sb.toString(), ",");

        String from = "";
        TableName tableName = tClass.getAnnotation(TableName.class);
        if (tableName != null) {
            from = tableName.value();
        }
        return new QueryDescriptor(select, from, fieldMap);
    }

    public static String getDbFieldName(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        String dbFieldName = tableField == null ? field.getName() : tableField.value();

        if (StringUtils.startsWith(dbFieldName, "{") && StringUtils.endsWith(dbFieldName, "}")) {
            dbFieldName = StringUtils.removeStart(dbFieldName, "{");
            dbFieldName = StringUtils.removeEnd(dbFieldName, "}");
        } else {
            dbFieldName = StringUtils.wrapIfMissing(dbFieldName, '`');
        }
        return dbFieldName;
    }

    String getSelect() {
        return select;
    }

    String getFrom() {
        return from;
    }

    String getDbFieldName(String field) {
        String emptyField = "''";
        String dbDelimiter = ".";
        if (StringUtils.isBlank(field)) {
            return emptyField;
        }
        if (field.contains(dbDelimiter)) {
            return field;
        }

        String fieldName = StringUtils.wrapIfMissing(field, '`');
        return this.fieldMap.getOrDefault(fieldName, fieldName);
    }

    String formatFilters(FilterGroup group) {
        if (group == null) {
            return null;
        }

        return group.formatSQL(this.fieldMap);
    }
}
