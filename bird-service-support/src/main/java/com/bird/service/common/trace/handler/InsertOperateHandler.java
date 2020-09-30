package com.bird.service.common.trace.handler;

import com.bird.service.common.trace.define.ColumnDefinition;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shaojie
 */
public class InsertOperateHandler extends AbstractDatabaseOperateHandler{

    @Override
    protected String getTableName(Statement statement) {
        return ((Insert)statement).getTable().toString();
    }

    @Override
    protected List<String[]> getOldValue(Connection connection, String table, ColumnDefinition[] columns, Statement statement) {
        return Collections.emptyList();
    }

    @Override
    protected List<String[]> getNewValue(String table, Statement statement) {
        Insert insert = (Insert) statement;
        List<String[]> result = new ArrayList<>();
        insert.getItemsList().accept(new ItemsListVisitor() {
            @Override
            public void visit(SubSelect subSelect) {

            }

            @Override
            public void visit(ExpressionList expressionList) {
                resolveExpressions(result,table,insert.getColumns(),expressionList.getExpressions());
            }

            @Override
            public void visit(NamedExpressionList namedExpressionList) {
                resolveExpressions(result,table,insert.getColumns(),namedExpressionList.getExpressions());
            }

            @Override
            public void visit(MultiExpressionList multiExprList) {
                List<ExpressionList> exprList = multiExprList.getExprList();
                for (ExpressionList expressionList: exprList){
                    resolveExpressions(result,table,insert.getColumns(),expressionList.getExpressions());
                }
            }
        });
        return result;
    }

    private static void resolveExpressions(List<String[]> result,String table, List<Column> fullColumns,  List<Expression> expressions) {
        String[] values = findValues(table,fullColumns,expressions);
        result.add(values);
    }
}
