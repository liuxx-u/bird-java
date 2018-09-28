package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/9/28
 */
class QueryDescriptor implements Serializable {

    private static final Map<String, String> OPERATE_MAP = new HashMap<String, String>() {{
        put(FilterOperate.EQUAL, "=");
        put(FilterOperate.NOTEQUAL, "!=");
        put(FilterOperate.LESS, "<");
        put(FilterOperate.LESSOREQUAL, "<=");
        put(FilterOperate.GREATER, ">");
        put(FilterOperate.GREATEROREQUAL, ">=");
        put(FilterOperate.STARTWITH, "like");
        put(FilterOperate.ENDWITH, "like");
        put(FilterOperate.CONTAINS, "like");
    }};

    private String select;
    private String from;
    private Map<String, String> fieldMap;

    private QueryDescriptor(String select, String from, Map<String, String> fieldMap) {
        this.select = select;
        this.from = from;
        this.fieldMap = fieldMap;
    }

    static QueryDescriptor parseClass(Class<?> tClass) {
        Map<String, String> fieldMap = new HashMap<>(16);

        if (tClass == null) return null;
        Class tempClass = tClass;
        StringBuilder sb = new StringBuilder();

        while (tempClass != null && !tempClass.getName().equals(Object.class.getName())) {
            Field[] tempFields = tempClass.getDeclaredFields();
            for (Field field : tempFields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) continue;

                String fieldName = StringUtils.wrapIfMissing(field.getName(),'`');
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

    private static String getDbFieldName(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        String dbFieldName = tableField == null ? field.getName() : tableField.value();
        return StringUtils.wrapIfMissing(dbFieldName, '`');
    }

    String formatRules(List<FilterRule> rules) {
        StringBuilder sb = new StringBuilder();

        boolean isStart = true;
        for (FilterRule rule : rules) {
            String field = StringUtils.strip(rule.getField());
            String value = StringUtils.strip(rule.getValue());
            if (StringUtils.isBlank(field) || StringUtils.isBlank(value)) continue;
//            if (!validateSqlValue(value)) continue;

            if (!OPERATE_MAP.containsKey(rule.getOperate())) {
                rule.setOperate(FilterOperate.EQUAL);
            }
            if (!isStart) {
                sb.append(" and ");
            }

            switch (rule.getOperate()) {
                case FilterOperate.STARTWITH:
                    value = value + "%";
                    break;
                case FilterOperate.ENDWITH:
                    value = "%" + value;
                    break;
                case FilterOperate.CONTAINS:
                    value = "%" + value + "%";
                    break;
                default:
                    break;
            }

            sb.append(this.getDbFieldName(field)).append(OPERATE_MAP.get(rule.getOperate())).append(" '").append(value).append("'");
            isStart = false;
        }
        return sb.toString();
    }


    String getSelect() {
        return select;
    }

    String getFrom() {
        return from;
    }

    String getDbFieldName(String field) {
        String emptyField = "''";
        if (StringUtils.isBlank(field)) return emptyField;

        String fieldName = StringUtils.wrapIfMissing(field, '`');
        return this.fieldMap.getOrDefault(fieldName, fieldName);
    }
}
