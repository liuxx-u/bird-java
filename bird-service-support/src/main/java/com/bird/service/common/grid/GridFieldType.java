package com.bird.service.common.grid;

import lombok.Getter;

import java.sql.SQLType;
import java.sql.Types;

/**
 * 表格支持的数据库字段类型
 *
 * @author liuxx
 * @since 2021/1/27
 */
@Getter
public enum GridFieldType implements SQLType {

    /**
     * Identifies the generic SQL type {@code INTEGER}.
     */
    INTEGER(Types.INTEGER),
    /**
     * Identifies the generic SQL type {@code BIGINT}.
     */
    BIGINT(Types.BIGINT),
    /**
     * Identifies the generic SQL type {@code FLOAT}.
     */
    FLOAT(Types.FLOAT),
    /**
     * Identifies the generic SQL type {@code DOUBLE}.
     */
    DOUBLE(Types.DOUBLE),
    /**
     * Identifies the generic SQL type {@code DECIMAL}.
     */
    DECIMAL(Types.DECIMAL),
    /**
     * Identifies the generic SQL type {@code VARCHAR}.
     */
    VARCHAR(Types.VARCHAR),
    /**
     * Identifies the generic SQL type {@code TIMESTAMP}.
     */
    TIMESTAMP(Types.TIMESTAMP),
    /**
     * Identifies the generic SQL type {@code BOOLEAN}.
     */
    BOOLEAN(Types.BOOLEAN),
    /**
     * Identifies the generic SQL value {@code NULL}.
     */
    NULL(Types.NULL);

    /**
     * The Integer value for the JDBCType.  It maps to a value in
     * {@code Types.java}
     */
    private Integer type;

    /**
     * Constructor to specify the data type value from {@code Types) for
     * this data type.
     * @param type The value from {@code Types) for this data type
     */
    GridFieldType(final Integer type) {
        this.type = type;
    }

    /**
     *{@inheritDoc }
     * @return The name of this {@code SQLType}.
     */
    @Override
    public String getName() {
        return name();
    }

    /**
     * Returns the name of the vendor that supports this data type. The value
     * returned typically is the package name for this vendor.
     *
     * @return The name of the vendor for this data type
     */
    @Override
    public String getVendor() {
        return "com.bird.service.common.grid";
    }

    /**
     * Returns the vendor specific type number for the data type.
     *
     * @return An Integer representing the vendor specific data type
     */
    @Override
    public Integer getVendorTypeNumber() {
        return this.type;
    }

    public static GridFieldType parse(String javaClass){
        switch (javaClass){
            case "java.lang.String":
                return GridFieldType.VARCHAR;
            case "int":
            case "java.lang.Integer":
                return GridFieldType.INTEGER;
            case "long":
            case "java.lang.Long":
                return GridFieldType.BIGINT;
            case "float":
            case "java.lang.Float":
                return GridFieldType.FLOAT;
            case "double":
            case "java.lang.Double":
                return GridFieldType.DOUBLE;
            case "boolean":
            case "java.lang.Boolean":
                return GridFieldType.BOOLEAN;
            case "java.util.Date":
            case "java.sql.Date":
                return GridFieldType.TIMESTAMP;
            case "java.math.BigDecimal":
                return GridFieldType.DECIMAL;
            default:
                return GridFieldType.NULL;
        }
    }

    /**
     * 是否可统计的类型
     * @param fieldType 列类型
     * @return 是否支持统计
     */
    public static boolean isSummable(GridFieldType fieldType){
        return fieldType == INTEGER || fieldType == BIGINT || fieldType == FLOAT || fieldType == DOUBLE || fieldType == DECIMAL;
    }
}
