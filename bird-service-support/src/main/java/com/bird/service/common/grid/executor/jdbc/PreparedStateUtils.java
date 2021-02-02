package com.bird.service.common.grid.executor.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/1
 */
public final class PreparedStateUtils {

    /**
     * 获取 {@link PreparedStatement} 实例
     *
     * @param connection     数据库连接
     * @param stateParameter SQL语句及参数信息
     * @return {@link PreparedStatement} 实例
     * @throws SQLException SQLException
     */
    public static PreparedStatement prepareStatement(Connection connection, PreparedStateParameter stateParameter) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(stateParameter.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        int index = 0;
        for (PreparedStateParameter.TypedParameter typedParameter : stateParameter.getParameters()) {
            statement.setObject(index++, typedParameter.getParameter(), typedParameter.getFieldType());
        }

        return statement;
    }

    /**
     * 读取{@link ResultSet}数据，并转为List
     *
     * @param resultSet 查询结果集
     * @return List
     * @throws SQLException SQLException
     */
    public static List<Map<String, Object>> readResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> rowData = new HashMap<>(16);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}
