package com.bird.service.common.grid.executor.jdbc.mysql;

import com.bird.service.common.grid.executor.jdbc.AbstractGridSqlParser;
import com.bird.service.common.grid.executor.jdbc.AutoGridJdbcProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @since 2021/1/28
 */
public class GridMySqlParser extends AbstractGridSqlParser {

    public GridMySqlParser(AutoGridJdbcProperties gridJdbcProperties){
        super(gridJdbcProperties);
    }

    @Override
    protected String dbFormatField(String dbField) {
        return StringUtils.wrapIfMissing(dbField, "`");
    }
}
