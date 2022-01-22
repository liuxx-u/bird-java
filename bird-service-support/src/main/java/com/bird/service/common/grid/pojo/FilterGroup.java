package com.bird.service.common.grid.pojo;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 筛选条件组
 * @author liuxx
 * @date 2018/9/29
 */
@Getter
public class FilterGroup implements Serializable {

    /**
     * 预编译SQL过滤正则表达式
     */
    private final static Pattern sqlPattern = Pattern.compile(
            "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)",
            Pattern.CASE_INSENSITIVE);

    /**
     * 组内筛选条件
     */
    private List<FilterRule> rules;

    /**
     * 连接符，支持and、or
     */
    private String operate;

    /**
     * 连接的组
     */
    private List<FilterGroup> groups;

    public FilterGroup(List<FilterRule> rules) {
        this.rules = rules;
    }

    public void and(List<FilterGroup> groups) {
        this.operate = FilterOperate.AND.getValue();
        this.groups = groups;
    }

    public void or(List<FilterGroup> groups) {
        this.operate = FilterOperate.OR.getValue();
        this.groups = groups;
    }

    /**
     * 解析为SQL语句
     *
     * @return sql
     */
    public String formatSQL() {
        return this.formatSQL(new HashMap<>());
    }

    /**
     * 解析为SQL语句
     *
     * @param fieldNameMap map
     * @return sql
     */
    public String formatSQL(Map<String, String> fieldNameMap) {
        String where = formatRules(this.rules, fieldNameMap);

        boolean isStart = true;
        StringBuilder groupBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(this.groups)) {
            for (FilterGroup innerGroup : this.groups) {
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
            FilterOperate groupOperate = FilterOperate.resolveOrDefault(this.operate, FilterOperate.AND);
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
            if(sqlPattern.matcher(field).find() || sqlPattern.matcher(value).find()){
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
