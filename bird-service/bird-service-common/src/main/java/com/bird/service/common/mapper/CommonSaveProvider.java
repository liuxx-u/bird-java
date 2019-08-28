package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.bird.service.common.service.dto.EntityDTO;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author liuxx
 * @date 2017/10/20
 */
public class CommonSaveProvider {
    /**
     * 不允许修改的列
     */
    private static final List<String> STATIC_FIELDS = Arrays.asList("`id`","`createTime`","`modifiedTime`");

    private static final List<String> STRING_TYPE_NAME = Collections.singletonList("java.lang.String");
    private static final List<String> NUMBER_TYPE_NAME = Arrays.asList("java.lang.Integer", "java.lang.Long", "java.math.BigDecimal", "int", "long");
    private static final List<String> BOOLEAN_TYPE_NAME = Arrays.asList("java.lang.Boolean", "boolean");
    private static final List<String> DATE_TYPE_NAME = Arrays.asList("java.util.Date", "java.sql.Date");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String insert(CommonSaveParam param) {
        String tableName = formatName(param.getTableName());

        StringBuilder sb = new StringBuilder("insert into ")
                .append(tableName)
                .append(" (");

        Field[] fields = param.gettClass().getDeclaredFields();

        StringBuilder valueBuilder = new StringBuilder();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;

            String fieldName = tableField == null ? field.getName() : tableField.value();
            //处理适用于多表的DTO
            if (fieldName.indexOf(".") > 0) {
                String[] arr = fieldName.split("\\.");
                if (!StringUtils.equals(formatName(arr[arr.length - 2]), tableName)) continue;

                fieldName = arr[arr.length - 1];
            }

            fieldName = formatName(fieldName);
            if (STATIC_FIELDS.contains(fieldName)) continue;

            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            if (Objects.equals(fieldValue, "") || Objects.equals(fieldValue, "''") || Objects.equals(fieldValue, "null")) continue;

            sb.append(fieldName).append(",");
            valueBuilder.append(fieldValue).append(",");
        }
        String createTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("createTime) values (").append(valueBuilder.toString()).append(createTime).append(")");
        return sb.toString();
    }

    public String update(CommonSaveParam param) {
        Long id = param.getEntityDTO().getId();
        if (id == null || id <= 0) return "";

        String tableName = formatName(param.getTableName());

        StringBuilder sb = new StringBuilder("update ")
                .append(tableName)
                .append(" set ");

        Field[] fields = param.gettClass().getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;
            String fieldName = tableField == null ? field.getName() : tableField.value();
            //处理适用于多表的DTO
            if (fieldName.indexOf(".") > 0) {
                String[] arr = fieldName.split("\\.");
                if (!StringUtils.equals(formatName(arr[arr.length - 2]), tableName)) continue;

                fieldName = arr[arr.length - 1];
            }

            fieldName = formatName(fieldName);
            if (STATIC_FIELDS.contains(fieldName)) continue;

            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            sb.append(fieldName).append(" = ").append(fieldValue).append(",");
        }
        String modifiedTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("modifiedTime = ").append(modifiedTime);
        sb.append(" where id = ").append(id);

        return sb.toString();
    }

    private String getFieldValue(EntityDTO instance, Field field) {
        field.setAccessible(true);
        String fieldTyppeName = field.getType().getName();

        try {
            Object value = field.get(instance);

            if (STRING_TYPE_NAME.contains(fieldTyppeName)) {
                return value == null ? "''" : "'" + value.toString() + "'";
            } else if (NUMBER_TYPE_NAME.contains(fieldTyppeName)) {
                return value == null ? "0" : value.toString();
            } else if (BOOLEAN_TYPE_NAME.contains(fieldTyppeName)) {
                if (value == null) return "0";
                return ((Boolean) value) ? "1" : "0";
            } else if (DATE_TYPE_NAME.contains(fieldTyppeName)) {
                return value == null ? "null" : "'" + dateFormat.format((Date) value) + "'";
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    private String formatName(String dbFieldName) {

        if (!StringUtils.startsWith(dbFieldName, "`")) {
            dbFieldName = "`" + dbFieldName;
        }
        if (!StringUtils.endsWith(dbFieldName, "`")) {
            dbFieldName += "`";
        }
        return dbFieldName;
    }
}