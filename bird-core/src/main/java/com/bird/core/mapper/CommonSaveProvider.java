package com.bird.core.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.bird.core.service.EntityDTO;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxx on 2017/10/20.
 */
public class CommonSaveProvider {
    private static final List<String> STRING_TYPE_NAME = Arrays.asList("java.lang.String");
    private static final List<String> NUMBER_TYPE_NAME = Arrays.asList("java.lang.Integer", "java.lang.Long", "java.math.BigDecimal", "int", "long");
    private static final List<String> BOOLEAN_TYPE_NAME = Arrays.asList("java.lang.Boolean", "boolean");
    private static final List<String> DATE_TYPE_NAME = Arrays.asList("java.util.Date", "java.sql.Date");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String insert(CommonSaveParam param) {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(param.getTableName());
        sb.append(" (");
        Field[] fields = param.gettClass().getDeclaredFields();

        StringBuilder valueBuilder = new StringBuilder();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;
            String fieldName = tableField == null ? field.getName() : tableField.value();

            if (fieldName == "id" || fieldName == "createTime" || fieldName == "modifiedTime") continue;
            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            if (fieldValue == "" || fieldValue == "''") continue;

            sb.append(getDbFieldName(fieldName) + ",");
            valueBuilder.append(fieldValue + ",");
        }
        String createTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("createTime) values (" + valueBuilder.toString() + createTime + ")");
        return sb.toString();
    }

    public String update(CommonSaveParam param) {
        Long id = param.getEntityDTO().getId();
        if (id == null || id <= 0) return "";

        StringBuilder sb = new StringBuilder("update ");
        sb.append(param.getTableName());
        sb.append(" set ");

        Field[] fields = param.gettClass().getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;
            String fieldName = tableField == null ? field.getName() : tableField.value();

            if (fieldName == "id" || fieldName == "createTime" || fieldName == "modifiedTime") continue;
            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            sb.append(getDbFieldName(fieldName) + " = " + fieldValue);
            sb.append(",");
        }
        String modifiedTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("modifiedTime = " + modifiedTime);
        sb.append(" where id = " + id);

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
                return ((Boolean) value) == true ? "1" : "0";
            } else if (DATE_TYPE_NAME.contains(fieldTyppeName)) {
                return value == null ? "''" : "'" + dateFormat.format((Date) value) + "'";
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    private String getDbFieldName(String fieldName) {
        String dbFieldName = fieldName;
        if (!StringUtils.startsWith(dbFieldName, "`")) {
            dbFieldName = "`" + dbFieldName;
        }
        if (!StringUtils.endsWith(dbFieldName, "`")) {
            dbFieldName = dbFieldName + "`";
        }
        return dbFieldName;
    }
}