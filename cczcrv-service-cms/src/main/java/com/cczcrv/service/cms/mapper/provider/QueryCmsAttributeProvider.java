package com.cczcrv.service.cms.mapper.provider;

import com.cczcrv.core.mapper.AbstractMybatisProvider;

/**
 * Created by liuxx on 2017/6/27.
 */
public class QueryCmsAttributeProvider extends AbstractMybatisProvider {

    public String QueryCmsAttributes(long[] ids, String[] keys) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CA.[ContentId] AS id,A.[Key] AS [key],CA.[Value] AS value\n" +
                "FROM [dbo].[Cms_ContentAttribute] AS CA \n" +
                "LEFT JOIN  [dbo].[Cms_Attribute] AS A ON CA.[AttributeId]=A.[Id]\n" +
                "WHERE CA.ContentId IN (" + ExpandIdAndToString(ids) + ") AND A.[Key] IN (" + ExpandKeysAndToString(keys) + ")");
        return sql.toString();
    }
}
