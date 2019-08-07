package com.bird.trace.client.sql;

import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Data
public class TraceSQL {

    /**
     * 数据库
     */
    private String database;
    /**
     * SQL语句
     */
    private String sql;
    /**
     * 开始时间 时间戳
     */
    private long start;
    /**
     * 结束时间 时间戳
     */
    private long end;
    /**
     * 耗时 毫秒
     */
    private long elapsed;
    /**
     * 错误信息
     */
    private String error;

    public TraceSQL() {
    }

    public TraceSQL(String database,String sql) {
        this.database = database;
        this.sql = sql;
    }
}
