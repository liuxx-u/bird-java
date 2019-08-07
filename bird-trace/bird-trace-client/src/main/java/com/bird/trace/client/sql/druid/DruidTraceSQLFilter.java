package com.bird.trace.client.sql.druid;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.bird.trace.client.TraceContext;
import com.bird.trace.client.sql.TraceSQL;
import com.bird.trace.client.sql.TraceSQLType;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author liuxx
 * @date 2019/8/5
 */
@Slf4j
public class DruidTraceSQLFilter extends FilterEventAdapter {

    private final static String ATTRIBUTE_KEY = "traceSQL";
    private static boolean mysql8Available;

    static {
        try {
            mysql8Available = null != Class.forName("com.mysql.cj.jdbc.ClientPreparedStatement");
        } catch (Exception t) {
            mysql8Available = false;
        }
    }

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        this.beforeStatementExecute(statement);
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        this.afterStatementExecute(statement, null);
    }

    @Override
    protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
        this.beforeStatementExecute(statement);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        this.afterStatementExecute(statement, null);
    }

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        this.beforeStatementExecute(statement);
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
        this.afterStatementExecute(statement, null);
    }

    @Override
    protected void statementExecuteBatchBefore(StatementProxy statement) {
        this.beforeStatementExecute(statement);
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        this.afterStatementExecute(statement, null);
    }

    @Override
    protected void statement_executeErrorAfter(StatementProxy statement, String sql, Throwable error) {
        this.afterStatementExecute(statement, error);
    }

    /**
     * SQL语句执行前操作
     *
     * @param statement statement
     */
    private void beforeStatementExecute(StatementProxy statement) {
        if (!this.isSupport(statement.getLastExecuteSql())) {
            return;
        }

        Statement rawObject = statement.getRawObject();
        String sql;
        String database = null;
        try {
            if (mysql8Available && rawObject instanceof ClientPreparedStatement) {
                sql = ((ClientPreparedStatement) rawObject).asSql();
            } else {
                sql = rawObject.toString();
            }
            database = statement.getConnection().getCatalog();
        } catch (SQLException e) {
            sql = rawObject.toString();
            log.error(e.getSQLState(), e);
        }
        TraceSQL traceSQL = new TraceSQL(database, sql);
        traceSQL.setStart(System.currentTimeMillis());

        statement.putAttribute(ATTRIBUTE_KEY, traceSQL);
    }

    /**
     * SQL语句执行后操作
     *
     * @param statement statement
     * @param error     error
     */
    private void afterStatementExecute(StatementProxy statement, Throwable error) {
        TraceSQL traceSQL = (TraceSQL) statement.getAttribute(ATTRIBUTE_KEY);
        if (traceSQL == null) return;
        traceSQL.setEnd(System.currentTimeMillis());
        traceSQL.setElapsed(traceSQL.getEnd() - traceSQL.getStart());
        if (error != null) {
            traceSQL.setError(error.getMessage());
        }

        TraceContext.appendSQL(traceSQL);
    }

    /**
     * 是否支持记录该SQL语句
     *
     * @param sql sql
     * @return true or false
     */
    private boolean isSupport(String sql) {
        TraceSQLType sqlType = TraceSQLType.acquire(sql);
        return TraceContext.isSupport(sqlType);
    }
}
