package com.bird.service.common.mapper.record;

import java.sql.Connection;

/**
 * @author shaojie
 */
public interface DatabaseOperateHandler {
    /**
     * 记录数据库操作
     * @param connection 数据库连接
     * @param operateSql 操作的SQL
     * @param operate 操作
     */
    void record(Connection connection, String operateSql, String operate);
}
