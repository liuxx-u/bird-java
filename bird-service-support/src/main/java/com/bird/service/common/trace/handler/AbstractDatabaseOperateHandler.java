package com.bird.service.common.trace.handler;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.bird.service.common.trace.FieldTraceAppender;
import com.bird.service.common.trace.IDatabaseOperateHandler;
import com.bird.service.common.trace.TraceField;
import com.bird.service.common.trace.define.FieldDefinition;
import com.bird.service.common.trace.define.FieldTraceDefinition;
import com.bird.service.common.trace.transaction.FieldTraceTransactionSynchronization;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shaojie
 */
@Slf4j
public abstract class AbstractDatabaseOperateHandler implements IDatabaseOperateHandler {

    final static String SELECT_TEMPLATE = "SELECT %s FROM %s WHERE %s";
    private final static String PK_DESCRIPTION = "PK";

    /**
     *
     */
    private static final Map<String, FieldDefinition[]> TABLE_TRACE_FIELD_MAP = new HashMap<>();
    private static final Map<String, Map<String, Integer>> TABLE_TRACE_FIELD_INDEX_MAP = new HashMap<>();

    @Override
    public void record(Connection connection, String operateSql, String operate) {
        try {
            // 解析SQL
            Statement stmt = CCJSqlParserUtil.parse(operateSql);
            // 获取表名
            String table = StringUtils.strip(getTableName(stmt), StringPool.BACKTICK);
            // 获取要记录的列信息
            FieldDefinition[] traceFields = getTraceFields(table);
            if (traceFields == null || traceFields.length == 0) {
                return;
            }
            FieldTraceDefinition traceDefinition = new FieldTraceDefinition(operate, traceFields, table);
            List<String[]> oldValues = getOldValue(connection, table, traceFields, stmt);
            List<String[]> newValues = getNewValue(table, stmt);
            traceDefinition.setSql(operateSql).setOld(oldValues).setNews(newValues);

            if (connection.getAutoCommit()) {
                // 如果是自动提交,说明不存在事务 直接放入当前的trace信息中
                FieldTraceAppender.append(traceDefinition);
            } else {
                // 手动提交, 说明存在事务, 此时需要通过Spring的事务框架来进行拓展
                FieldTraceTransactionSynchronization.appendResource(traceDefinition);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据SQL获取表名
     *
     * @param statement SQL语句信息
     * @return 表名
     */
    protected abstract String getTableName(Statement statement);

    /**
     * 获取操作之后的值
     *
     * @param table     操作的表
     * @param statement 操作语句
     * @return 操作之后的值列表
     */
    protected abstract List<String[]> getNewValue(String table, Statement statement);

    /**
     * 获取操作之前的值
     *
     * @param connection 数据库连接
     * @param table      表
     * @param fields    要获取的列
     * @param statement  操作SQL
     * @return 对应的之前的值
     */
    protected abstract List<String[]> getOldValue(Connection connection, String table, FieldDefinition[] fields, Statement statement);

    List<String[]> queryOldValues(Connection connection, String querySql, int length) {
        java.sql.Statement sqlStatement = null;
        try {
            sqlStatement = connection.createStatement();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        List<String[]> list = new ArrayList<>();
        Optional.ofNullable(sqlStatement).ifPresent(stmt -> {
            try {
                stmt.execute(querySql);
                ResultSet resultSet = stmt.getResultSet();
                resolveResult(resultSet, list, length);
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        });
        return list;
    }

    String[] findNewValues(String table, List<Column> updateColumns, List<Expression> expressions) {
        Map<String, Integer> fieldIndexMap = getTableFieldMapping(table);
        String[] values = new String[fieldIndexMap.size()];
        Integer index;
        for (int i = 0; i < updateColumns.size(); i++) {
            String columnName = StringUtils.strip(updateColumns.get(i).toString(), StringPool.BACKTICK);
            index = fieldIndexMap.get(columnName);
            if (index != null) {
                // 本次更新有要记录的字段
                Expression expression = expressions.get(i);
                values[index] = expression.toString();
            }
        }
        return values;
    }

    String toFieldsQuery(FieldDefinition[] fields) {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                stb.append(",");
            }
            stb.append(fields[i].getName());
        }
        return stb.toString();
    }

    private void resolveResult(ResultSet resultSet, List<String[]> list, int length) throws SQLException {
        String[] value;
        while (resultSet.next()) {
            value = new String[length];
            for (int i = 0; i < length; i++) {
                value[i] = resultSet.getString(i + 1);
            }
            list.add(value);
        }
    }

    private FieldDefinition[] getTraceFields(String table) {
        return TABLE_TRACE_FIELD_MAP.computeIfAbsent(table, key -> {
            FieldDefinition[] fieldDefinitions = new FieldDefinition[]{};

            List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
            Optional<TableInfo> optional = tableInfos.stream().filter(info -> table.equals(info.getTableName())).findFirst();
            if (optional.isPresent()) {
                TableInfo tableInfo = optional.get();
                List<TableFieldInfo> fieldList = tableInfo.getFieldList();

                List<FieldDefinition> traceFields = fieldList.stream()
                        .filter(field -> field.getField().isAnnotationPresent(TraceField.class))
                        .map(field -> new FieldDefinition(field.getField()))
                        .collect(Collectors.toList());

                //默认添加主键的跟踪
                traceFields.add(new FieldDefinition(tableInfo.getKeyColumn(), PK_DESCRIPTION));
                return traceFields.toArray(fieldDefinitions);
            }
            return fieldDefinitions;
        });
    }

    private Map<String, Integer> getTableFieldMapping(String table) {
        return TABLE_TRACE_FIELD_INDEX_MAP.computeIfAbsent(table, key -> {
            FieldDefinition[] fields = getTraceFields(key);
            Map<String, Integer> map = new HashMap<>(fields.length);
            for (int i = 0; i < fields.length; i++) {
                map.put(fields[i].getName(), i);
            }
            return map;
        });
    }
}
