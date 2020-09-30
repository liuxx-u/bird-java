package com.bird.service.common.trace.handler;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.bird.service.common.trace.IDatabaseOperateHandler;
import com.bird.service.common.trace.ColumnTrace;
import com.bird.service.common.trace.ColumnTraceExchanger;
import com.bird.service.common.trace.define.ColumnDefinition;
import com.bird.service.common.trace.define.ColumnTraceDefinition;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author shaojie
 */
@Slf4j
public abstract class AbstractDatabaseOperateHandler implements IDatabaseOperateHandler {

    static final String SELECT_TEMPLATE = "SELECT %s FROM %s WHERE %s";
    /**  */
    private static final Map<String,Map<String,Integer>> RECORD_COLUMN_MAPPING = new HashMap<>();
    private static final Map<String,ColumnDefinition[]> RECORD_COLUMNS = new HashMap<>();

    @Override
    public void record(Connection connection,String operateSql,String operate) {
        try {
            // 解析SQL
            Statement stmt = CCJSqlParserUtil.parse(operateSql);
            // 获取表名
            String table = getTableName(stmt);
            // 获取要记录的列信息
            ColumnDefinition[] columns = getRecordColumns(table);
            if(columns == null || columns.length == 0){
                return;
            }
            ColumnTraceDefinition columnTraceDefinition = new ColumnTraceDefinition(operate, columns, table);
            List<String[]> oldValues = getOldValue(connection,table,columns,stmt);
            List<String[]> newValues = getNewValue(table,stmt);
            columnTraceDefinition.setOld(oldValues).setNews(newValues);
            // 放入队列中, 等待被记录
            ColumnTraceExchanger.offer(columnTraceDefinition);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 获取操作之后的值
     * @param table 操作的表
     * @param statement 操作语句
     * @return 操作之后的值列表
     */
    protected abstract List<String[]> getNewValue(String table,Statement statement);

    /**
     * 获取操作之前的值
     * @param connection 数据库连接
     * @param table 表
     * @param columns 要获取的列
     * @param statement 操作SQL
     * @return 对应的之前的值
     */
    protected abstract List<String[]> getOldValue(Connection connection,String table, ColumnDefinition[] columns, Statement statement);


    public List<String[]> query(Connection connection,String querySql,int length){
        java.sql.Statement sqlStatement = null;
        try {
            sqlStatement= connection.createStatement();
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        List<String[]> list = new ArrayList<>();
        Optional.ofNullable(sqlStatement).ifPresent(stmt->{
            try {
                stmt.execute(querySql);
                ResultSet resultSet = stmt.getResultSet();
                resolveResult(resultSet,list,length);
            } catch (SQLException e) {
                log.error(e.getMessage(),e);
            }
        });
        return list;
    }

    private static void resolveResult(ResultSet resultSet, List<String[]> list, int length) throws SQLException {
        String[] value;
        while(resultSet.next()){
            value = new String[length];
            for(int i =0;i< length;i++){
                value[i] = resultSet.getString(i + 1);
            }
            list.add(value);
        }
    }


    static String[] findValues(String table, List<Column> updateColumns, List<Expression> expressions){
        Map<String, Integer> columnMapping = getTableColumnMapping(table);
        String[] values = new String[columnMapping.size()];
        Integer index;
        for (int i =0;i < updateColumns.size();i++){
            index = columnMapping.get(updateColumns.get(i).toString());
            if(index != null){
                // 本次更新有要记录的字段
                Expression expression = expressions.get(i);
                values[index] = expression.toString();
            }
        }
        return values;
    }

    /**
     * 根据SQL获取表名
     * @param statement SQL语句信息
     * @return 表名
     */
    protected abstract String getTableName(Statement statement);


    private static ColumnDefinition[] getRecordColumns(String table){
        return RECORD_COLUMNS.computeIfAbsent(table,key->{
            List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
            Optional<TableInfo> optional = tableInfos.stream().filter(info -> table.equals(info.getTableName())).findFirst();
            if(optional.isPresent()){
                TableInfo tableInfo = optional.get();
                List<TableFieldInfo> fieldList = tableInfo.getFieldList();
                return (ColumnDefinition[]) fieldList.stream().filter(field-> field.getField().isAnnotationPresent(ColumnTrace.class))
                        .map(field-> new ColumnDefinition(field.getField()))
                        .toArray();
            }
            return new ColumnDefinition[]{};
        });
    }

    private static Map<String,Integer> getTableColumnMapping(String table){
        return RECORD_COLUMN_MAPPING.computeIfAbsent(table,key->{
            ColumnDefinition[] columns = getRecordColumns(key);
            Map<String,Integer> map = new HashMap<>(columns.length);
            for (int i = 0; i < columns.length; i++) {
                map.put(columns[i].getName(),i);
            }
            return map;
        });
    }

    static String toColumnQuery(ColumnDefinition[] columns){
        StringBuilder stb = new StringBuilder();
        for (int i =0;i < columns.length;i++){
            if(i != 0){
                stb.append(",");
            }
            stb.append(columns[i].getName());
        }
        return stb.toString();
    }
}
