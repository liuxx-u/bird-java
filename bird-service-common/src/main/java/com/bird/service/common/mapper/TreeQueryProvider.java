package com.bird.service.common.mapper;

import com.bird.core.Check;
import org.apache.commons.lang3.StringUtils;

public class TreeQueryProvider {
    public String queryTreeData(TreeQueryParam param) {
        Check.NotEmpty(param.getValueField(), "valueField");
        Check.NotEmpty(param.getTextField(), "textField");
        Check.NotEmpty(param.getFrom(), "from");


        StringBuilder sb = new StringBuilder("select ");
        sb.append(getSafeFieldName(param.getValueField()) + " as value,");
        sb.append(getSafeFieldName(param.getTextField()) + " as label,");
        sb.append(getSafeFieldName(param.getParentValueField()) + " as parentValue");
        sb.append(" from " + param.getFrom());
        sb.append(" where delFlag = 0");
        if (StringUtils.isNotBlank(param.getWhere())) {
            sb.append(" and " + param.getWhere());
        }

        if(StringUtils.isNotBlank(param.getOrderBy())) {
            sb.append(" order by " + param.getOrderBy());
        }

        return sb.toString();
    }

    private String getSafeFieldName(String fieldName) {
        if (StringUtils.isBlank(fieldName)) return "'0'";

        if (!fieldName.startsWith("`")) {
            fieldName = "`" + fieldName;
        }
        if (!fieldName.endsWith("`")) {
            fieldName = fieldName + "`";
        }
        return fieldName;
    }
}
