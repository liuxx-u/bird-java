package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.query.FilterGroup;
import com.bird.service.common.grid.query.FilterOperate;
import com.bird.service.common.grid.query.FilterRule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractFilterGroupParser implements IFilterGroupParser {

    @Override
    public String formatSQL(FilterGroup filterGroup, Map<String, String> fieldNameMap) {
        String where = formatRules(filterGroup.getRules(), fieldNameMap);

        boolean isStart = true;
        StringBuilder groupBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(filterGroup.getGroups())) {
            for (FilterGroup innerGroup : filterGroup.getGroups()) {
                if (innerGroup == null) {
                    continue;
                }
                String innerWhere = innerGroup.formatSQL(fieldNameMap);
                if (StringUtils.isNotBlank(innerWhere)) {
                    if (!isStart) {
                        groupBuilder.append(" and ");
                    }
                    groupBuilder.append(innerWhere);
                    isStart = false;
                }
            }
        }
        String groupWhere = groupBuilder.toString();

        if (StringUtils.isBlank(groupWhere)) {
            return where;
        } else if (StringUtils.isBlank(where)) {
            return groupWhere;
        } else {
            StringBuilder sb = new StringBuilder();
            FilterOperate groupOperate = FilterOperate.resolveOrDefault(filterGroup.getOperate(), FilterOperate.AND);
            if (groupOperate == null) {
                groupOperate = FilterOperate.AND;
            }

            sb.append("(").append(where).append(" ").append(groupOperate.getDbValue()).append(" ").append(groupWhere).append(")");
            return sb.toString();
        }
    }

    private String formatRules(List<FilterRule> rules, Map<String, String> fieldNameMap) {
        if (CollectionUtils.isEmpty(rules)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();

        boolean isStart = true;
        for (FilterRule rule : rules) {
            if (rule == null) {
                continue;
            }

            String field = StringUtils.strip(rule.getField());
            String value = StringUtils.strip(rule.getValue());
            if (StringUtils.isBlank(field) || StringUtils.isBlank(value)) {
                continue;
            }
            if (!isStart) {
                sb.append(" and ");
            }

            FilterOperate ruleOperate = FilterOperate.resolve(rule.getOperate());
            if (ruleOperate == null) {
                ruleOperate = FilterOperate.EQUAL;
            }

            String dbFieldName = this.getDbFieldName(fieldNameMap, field);

            if (ruleOperate == FilterOperate.IN) {
                sb.append(String.format("FIND_IN_SET(%s,%s)", dbFieldName, StringUtils.wrapIfMissing(StringUtils.strip(value, ","), "'")));
            } else {
                switch (ruleOperate) {
                    case START_WITH:
                        value = value + "%";
                        break;
                    case END_WITH:
                        value = "%" + value;
                        break;
                    case CONTAINS:
                        value = "%" + value + "%";
                        break;
                    default:
                        break;
                }
                value = StringUtils.wrapIfMissing(value, "'");
                sb.append(dbFieldName).append(" ").append(ruleOperate.getDbValue()).append(" ").append(value);
            }
            isStart = false;
        }
        String where = sb.toString();
        return StringUtils.isNotBlank(where) ? "(" + where + ")" : where;
    }

    /**
     * 获取字段对应的数据库列名
     *
     * @param fieldNameMap map
     * @param field        field
     * @return db field name
     */
    private String getDbFieldName(Map<String, String> fieldNameMap, String field) {
        if (MapUtils.isEmpty(fieldNameMap)) {
            return field;
        }
        return fieldNameMap.getOrDefault(StringUtils.wrapIfMissing(field, "`"), field);
    }
}
