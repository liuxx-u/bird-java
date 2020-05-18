package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bird.service.common.incrementer.UUIDHexGenerator;
import com.bird.service.common.service.dto.IEntityDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author liuxx
 * @date 2017/10/20
 */
@Slf4j
public class CommonSaveProvider {

    private static final List<String> EXCLUDE_FIELD_NAMES;

    static {
        EXCLUDE_FIELD_NAMES = Collections.singletonList("serialVersionUID");
    }

    /**
     * 不允许修改的列
     */
    private static final List<String> STATIC_FIELDS = Arrays.asList("`id`", "`createTime`", "`modifiedTime`");

    private static final List<String> STRING_TYPE_NAME = Collections.singletonList("java.lang.String");
    private static final List<String> NUMBER_TYPE_NAME = Arrays.asList("java.lang.Integer", "java.lang.Long", "java.math.BigDecimal", "int", "long");
    private static final List<String> BOOLEAN_TYPE_NAME = Arrays.asList("java.lang.Boolean", "boolean");
    private static final List<String> DATE_TYPE_NAME = Arrays.asList("java.util.Date", "java.sql.Date");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String insert(CommonSaveParam param) {

        Map<String, String> fieldValueMap = this.getFieldValueMap(param);
        if (param.getEntityDTO().getId() instanceof String) {
            String id = param.getEntityDTO().getId().toString();
            if (StringUtils.isBlank(id)) {
                id = UUIDHexGenerator.generate();
            }
            fieldValueMap.put("`id`", "'" + id + "'");
        }

        String tableName = formatName(param.getTableName());
        StringBuilder sb = new StringBuilder("insert into ")
                .append(tableName)
                .append(" (");
        StringBuilder valueBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
            String fieldValue = entry.getValue();
            if (Objects.equals(fieldValue, "") || Objects.equals(fieldValue, "''") || Objects.equals(fieldValue, "null"))
                continue;

            sb.append(entry.getKey()).append(",");
            valueBuilder.append(fieldValue).append(",");
        }
        String createTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("createTime) values (").append(valueBuilder.toString()).append(createTime).append(")");
        return sb.toString();
    }

    public String update(CommonSaveParam param) {
        Serializable id = param.getEntityDTO().getId();
        if (id == null) return "";

        Map<String, String> fieldValueMap = this.getFieldValueMap(param);
        if (MapUtils.isEmpty(fieldValueMap)) return "";

        String tableName = formatName(param.getTableName());

        StringBuilder sb = new StringBuilder("update ")
                .append(tableName)
                .append(" set ");
        for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append(",");
        }

        String modifiedTime = "'" + dateFormat.format(new Date()) + "'";
        sb.append("modifiedTime = ").append(modifiedTime);
        sb.append(" where id = ").append("'").append(id).append("'");

        return sb.toString();
    }

    /**
     * 获取字段名与值的映射关系
     *
     * @param param param
     * @return map
     */
    private Map<String, String> getFieldValueMap(CommonSaveParam param) {
        Map<String, String> map = new LinkedHashMap<>();

        String tableName = formatName(param.getTableName());

        Field[] fields = param.gettClass().getDeclaredFields();
        for (Field field : fields) {
            if (EXCLUDE_FIELD_NAMES.contains(field.getName())) continue;

            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) continue;
            String fieldName = tableField == null ? field.getName() : tableField.value();
            //处理适用于多表的DTO
            if (fieldName.contains(".")) {
                String[] arr = fieldName.split("\\.");
                if (!StringUtils.equals(formatName(arr[arr.length - 2]), tableName)) continue;

                fieldName = arr[arr.length - 1];
            }

            fieldName = formatName(fieldName);
            if (STATIC_FIELDS.contains(fieldName)) continue;

            String fieldValue = getFieldValue(param.getEntityDTO(), field);
            map.put(fieldName, fieldValue);
        }
        return map;
    }

    /**
     * 获取EntityDTO中的字段值
     *
     * @param instance dto
     * @param field    field
     * @return value
     */
    private String getFieldValue(IEntityDTO instance, Field field) {
        field.setAccessible(true);
        String fieldTypeName = field.getType().getName();

        try {
            Object value = field.get(instance);

            if (STRING_TYPE_NAME.contains(fieldTypeName)) {
                return value == null ? "''" : "'" + value.toString() + "'";
            } else if (NUMBER_TYPE_NAME.contains(fieldTypeName)) {
                return value == null ? "0" : value.toString();
            } else if (BOOLEAN_TYPE_NAME.contains(fieldTypeName)) {
                if (value == null) return "0";
                return ((Boolean) value) ? "1" : "0";
            } else if (DATE_TYPE_NAME.contains(fieldTypeName)) {
                return value == null ? "null" : "'" + dateFormat.format((Date) value) + "'";
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("insert error", e);
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