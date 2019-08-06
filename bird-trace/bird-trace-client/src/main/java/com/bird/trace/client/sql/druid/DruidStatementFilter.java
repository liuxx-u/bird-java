package com.bird.trace.client.sql.druid;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;

/**
 * @author liuxx
 * @date 2019/8/5
 */
public class DruidStatementFilter extends FilterEventAdapter {

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql){

    }

//    private
}
