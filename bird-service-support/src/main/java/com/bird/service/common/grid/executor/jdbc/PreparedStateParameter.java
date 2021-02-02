package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridFieldType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
public class PreparedStateParameter {

    /**
     * 参数化SQL
     */
    private String sql;
    /**
     * 参数集合
     */
    private List<TypedParameter> parameters;

    public PreparedStateParameter() {
        this.sql = StringUtils.EMPTY;
        this.parameters = new ArrayList<>();
    }

    public PreparedStateParameter(String sql) {
        this();
        this.sql = sql;
    }

    public PreparedStateParameter(String sql, List<TypedParameter> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    /**
     * 添加参数
     *
     * @param fieldType 参数类型
     * @param parameter 参数值
     */
    public void addParameter(GridFieldType fieldType, Object parameter) {
        TypedParameter typedParameter = new TypedParameter();
        typedParameter.fieldType = fieldType;
        typedParameter.parameter = parameter;
        parameters.add(typedParameter);
    }

    /**
     * 连接SQL字符串
     *
     * @param sql sql
     */
    public PreparedStateParameter append(String sql) {
        this.sql += sql;
        return this;
    }

    /**
     * 连接{@link PreparedStateParameter} 参数
     *
     * @param stateParameter 参数
     */
    public PreparedStateParameter append(PreparedStateParameter stateParameter) {
        this.sql += stateParameter.sql;
        this.parameters.addAll(stateParameter.parameters);
        return this;
    }

    /**
     * sql是否为空
     *
     * @return sql是否为空
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(this.sql);
    }

    @Data
    public static class TypedParameter {

        /**
         * 字段类型
         */
        private GridFieldType fieldType;
        /**
         * 参数
         */
        private Object parameter;
    }
}
