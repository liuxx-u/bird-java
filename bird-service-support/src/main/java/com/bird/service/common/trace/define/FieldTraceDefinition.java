package com.bird.service.common.trace.define;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author shaojie
 */
@Setter
@Getter
@ToString@Accessors(chain = true)
public class FieldTraceDefinition {

    /**
     * 操作
     */
    private String operate;
    /**
     * 本次记录的表
     */
    private String table;
    /**
     * 执行的sql语句
     */
    private String sql;
    /**
     * 记录的列
     */
    private FieldDefinition[] fields;
    /**
     * 原始值
     */
    private List<String[]> old;
    /**
     * 更新后的值
     */
    private List<String[]> news;

    public FieldTraceDefinition(String operate, FieldDefinition[] fields, String table) {

        this.operate = operate;
        this.table = table;
        this.fields = fields;
    }
}
