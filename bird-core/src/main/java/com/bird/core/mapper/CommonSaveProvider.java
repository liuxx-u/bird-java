package com.bird.core.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.bird.core.service.EntityDTO;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuxx on 2017/10/20.
 */
public class CommonSaveProvider {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String insert(CommonSaveParam param) {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(param.getTableName());
        sb.append(" (");
        Field[] fields = param.gettClass().getDeclaredFields();

        StringBuilder valueBuilder = new StringBuilder();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            String fieldName = tableField == null ? field.getName() : tableField.value();

            if (fieldName == "id" || fieldName == "createTime") continue;
            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            if (fieldValue == "" || fieldValue == "''") continue;

            sb.append(fieldName + ",");
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
        int i = 0;
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            String fieldName = tableField == null ? field.getName() : tableField.value();

            if (fieldName == "id" || fieldName == "createTime") continue;
            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            sb.append(fieldName + " = " + fieldValue);
            if (i++ < fields.length - 1) sb.append(",");
        }
        sb.append(" where id = " + id);

        return sb.toString();
    }

    private String getFieldValue(EntityDTO instance, Field field) {
        field.setAccessible(true);
        String fieldTyppeName = field.getType().getName();

        try {
            if (fieldTyppeName.equals("java.lang.String")) {
                Object value = field.get(instance);
                return value == null ? "''" : "'" + field.get(instance).toString() + "'";
            } else if (fieldTyppeName.equals("java.lang.Integer") || fieldTyppeName.equals("int")) {
                return field.getInt(instance) + "";
            } else if (fieldTyppeName.equals("java.lang.Long") || fieldTyppeName.equals("long")) {
                return field.getLong(instance) + "";
            } else if (fieldTyppeName.equals("java.lang.Boolean") || fieldTyppeName.equals("boolean")) {
                return field.getBoolean(instance) ? "1" : "0";
            } else if (fieldTyppeName.equals("java.util.Date") || fieldTyppeName.equals("java.sql.Date")) {
                Object value = field.get(instance);
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
}
