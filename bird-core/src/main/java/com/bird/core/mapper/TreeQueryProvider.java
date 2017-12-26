package com.bird.core.mapper;

import com.bird.core.Check;
import com.bird.core.utils.StringHelper;

public class TreeQueryProvider {
    public String queryTreeData(TreeQueryParam param) {
        Check.NotEmpty(param.getValueField(), "valueField");
        Check.NotEmpty(param.getTextField(), "textField");
        Check.NotEmpty(param.getFrom(), "from");


        StringBuilder sb = new StringBuilder("select ");
        sb.append(getSafeFieldName(param.getValueField()) + " as value,");
        sb.append(getSafeFieldName(param.getTextField()) + " as text,");
        sb.append(getSafeFieldName(param.getParentValueField()) + " as parentValue");
        sb.append(" from " + param.getFrom());
        sb.append(" where delFlag = 0");
        if (!StringHelper.isNullOrWhiteSpace(param.getWhere())) {
            sb.append(" and " + param.getWhere());
        }

        if(!StringHelper.isNullOrWhiteSpace(param.getOrderBy())) {
            sb.append(" order by " + param.getOrderBy());
        }

        return sb.toString();
    }

    private String getSafeFieldName(String fieldName) {
        if (StringHelper.isNullOrWhiteSpace(fieldName)) return "'0'";

        if (!fieldName.startsWith("`")) {
            fieldName = "`" + fieldName;
        }
        if (!fieldName.endsWith("`")) {
            fieldName = fieldName + "`";
        }
        return fieldName;
    }
}
