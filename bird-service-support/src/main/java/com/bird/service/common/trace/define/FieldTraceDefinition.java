package com.bird.service.common.trace.define;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.core.trace.TraceContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author shaojie
 */
@Setter
@Getter
@ToString@Accessors(chain = true)
public class FieldTraceDefinition {

    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作人标识
     */
    private String operatorId;
    /**
     * 操作
     */
    private String operate;
    /**
     * 本次记录的表
     */
    private String table;
    /**
     * 拓展信息
     */
    private String extend;
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
        BirdSession session = SessionContext.getSession();
        if (session.getUserId() != null) {
            this.operator = StringUtils.defaultIfEmpty(session.getRealName(), session.getUserName());
            this.operatorId = session.getUserId().toString();
        }
        this.traceId = TraceContext.currentTraceId();

        this.operate = operate;
        this.table = table;
        this.fields = fields;
    }
}
