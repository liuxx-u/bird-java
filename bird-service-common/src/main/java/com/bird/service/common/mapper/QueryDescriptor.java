package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/9/28
 */
class QueryDescriptor {

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
        String select = "", from = "";
        Map<String, String> fieldMap = new HashMap<>(16);

        if (tClass == null) return null;
        Class tempClass = tClass;
        StringBuilder sb = new StringBuilder();

        while (tempClass != null && !tempClass.getName().equals(Object.class.getName())) {
            Field[] tempFields = tempClass.getDeclaredFields();
            for (Field field : tempFields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) continue;

                String fieldName = String.format("`%s`", field.getName());
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

        select = StringUtils.stripEnd(sb.toString(), ",");

        //如果from为空，则使用tClass的TableName
        TableName tableName = tClass.getAnnotation(TableName.class);
        if (tableName != null) {
            from = tableName.value();
        }
        return new QueryDescriptor(select, from, fieldMap);
    }

    private static String getDbFieldName(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        String dbFieldName = tableField == null ? field.getName() : tableField.value();
        if (!StringUtils.startsWith(dbFieldName, "`")) {
            dbFieldName = "`" + dbFieldName;
        }
        if (!StringUtils.endsWith(dbFieldName, "`")) {
            dbFieldName += "`";
        }
        return dbFieldName;
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

            if (rule.getOperate().equals(FilterOperate.STARTWITH)) {
                value = value + "%";
            } else if (rule.getOperate().equals(FilterOperate.ENDWITH)) {
                value = "%" + value;
            } else if (rule.getOperate().equals(FilterOperate.CONTAINS)) {
                value = "%" + value + "%";
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

        String fieldName = String.format("`%s`", field);
        return this.fieldMap.getOrDefault(fieldName, fieldName);
    }
}
