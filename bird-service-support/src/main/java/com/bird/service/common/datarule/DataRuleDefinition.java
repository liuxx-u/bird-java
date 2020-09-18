package com.bird.service.common.datarule;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;

/**
 * 规则元信息
 *
 * @author liuxx
 * @date 2018/10/10
 */
@Getter
@Setter
public class DataRuleDefinition implements Serializable {
    /**
     * 应用名
     */
    private String appName;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 类名称
     */
    private String className;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段名称
     */
    private String dbFieldName;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据源策略
     */
    private String sourceStrategy;
    /**
     * 数据源提供者类名
     */
    private String sourceProvider;
    /**
     * 数据源获取地址
     */
    private String sourceUrl;

    @Override
    public int hashCode() {
        return (this.appName + this.className + this.fieldName).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DataRuleDefinition)) {
            return false;
        }

        DataRuleDefinition other = (DataRuleDefinition) obj;
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
