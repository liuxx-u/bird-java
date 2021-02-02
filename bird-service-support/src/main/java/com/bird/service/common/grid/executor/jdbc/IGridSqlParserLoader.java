package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.executor.DialectType;

import java.util.Map;

/**
 * SQL解析选择器
 *
 * @author liuxx
 * @since 2021/2/1
 */
public interface IGridSqlParserLoader {

    /**
     * 加载SQL解析器集合
     *
     * @return SQL解析器映射Map
     */
    Map<DialectType, IGridSqlParser> loadSqlParsers();
}
