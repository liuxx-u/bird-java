package com.bird.service.common.grid.executor.jdbc.mysql;

import com.bird.service.common.grid.executor.jdbc.AbstractGridSqlParser;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @since 2021/1/28
 */
public class GridMySqlParser extends AbstractGridSqlParser {

    @Override
    protected String formatDbField(String dbField) {
        return StringUtils.wrapIfMissing(dbField, "`");
    }
}
