package com.bird.trace.client.sql;

import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Data
public class TraceSQL {

    /**
     * 数据库连接
     */
    private String connection;
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
}
