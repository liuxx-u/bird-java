package com.bird.service.common.trace.handler;

import com.bird.service.common.trace.define.FieldDefinition;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shaojie
 */
public class UpdateOperateHandler extends AbstractDatabaseOperateHandler {

    @Override
    protected String getTableName(Statement statement) {
        return ((Update)statement).getTable().toString();
    }

    @Override
    protected List<String[]> getNewValue(String table,Statement statement) {
        Update update = (Update) statement;
        List<String[]> list = new ArrayList<>();
        list.add(findNewValues(table,update.getColumns(),update.getExpressions()));
        return list;
    }

    @Override
    protected List<String[]> getOldValue(Connection connection, String table, FieldDefinition[] fields, Statement statement) {
        Update update = (Update) statement;
        String querySql = String.format(SELECT_TEMPLATE, toFieldsQuery(fields),table,update.getWhere().toString());
        return queryOldValues(connection,querySql, fields.length);
    }
}
