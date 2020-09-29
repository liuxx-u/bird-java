package com.bird.service.common.mapper.record;

import com.bird.core.session.SessionContext;
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
public class TableFieldRecord {

    /** 操作人 */
    private String operator;
    /** 操作 */
    private String operate;
    /** 本次记录的表 */
    private String table;
    /** 拓展信息 */
    private String extend;
    /** 记录的列 */
    private String[] column;
    /** 原始值 */
    private List<String[]> old;
    /** 更新后的值 */
    private List<String[]> news;

    public TableFieldRecord(String operator,String operate, String[] column, String table) {
        this.operator = operator;
        this.operate = operate;
        this.table = table;
        this.column = column;
    }

    public TableFieldRecord(String operate,String[] column, String table) {
        this(SessionContext.getUserId(),operate, column, table);
    }
}
