package com.bird.core.mapper;

import com.bird.core.Check;

/**
 * Created by liuxx on 2017/11/2.
 */
public class CommonDeleteProvider {

    public String delete(CommonDeleteParam param) {
        String tableName = param.getTableName();
        String where = param.getWhere();

        Check.NotEmpty(tableName, "tableName");
        Check.NotEmpty(where, "where");

        if (param.isSoftDelete()) {
            return "update " + tableName + " set delFlag=1 where " + where;
        } else {
            return "delete from " + tableName + " where " + where;
        }
    }
}
