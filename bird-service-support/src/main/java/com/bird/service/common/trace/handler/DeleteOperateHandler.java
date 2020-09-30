package com.bird.service.common.trace.handler;

import com.bird.service.common.trace.define.FieldDefinition;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * @author shaojie
 */
@Slf4j
public class DeleteOperateHandler extends AbstractDatabaseOperateHandler {

    @Override
    protected String getTableName(Statement statement) {
        return ((Delete)statement).getTable().toString();
    }

    @Override
    protected List<String[]> getOldValue(Connection connection, String table, FieldDefinition[] fields, Statement statement) {
        Delete delete = (Delete) statement;
        String querySql = String.format(SELECT_TEMPLATE, toFieldsQuery(fields),table,delete.getWhere().toString());
        return query(connection,querySql, fields.length);
    }

    @Override
    protected List<String[]> getNewValue(String table,Statement statement) {
        return Collections.emptyList();
    }
}
