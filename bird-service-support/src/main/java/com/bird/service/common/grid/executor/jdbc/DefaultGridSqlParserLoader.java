package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.executor.DialectType;
import com.bird.service.common.grid.executor.jdbc.mysql.GridMySqlParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/1
 */
public class DefaultGridSqlParserLoader implements IGridSqlParserLoader {

    /**
     * 加载SQL解析器集合
     *
     * @return SQL解析器映射Map
     */
    @Override
    public Map<DialectType, IGridSqlParser> loadSqlParsers() {
        Map<DialectType, IGridSqlParser> sqlParserMap = new HashMap<>(4);

        sqlParserMap.put(DialectType.MYSQL, new GridMySqlParser());
        return sqlParserMap;
    }
}
