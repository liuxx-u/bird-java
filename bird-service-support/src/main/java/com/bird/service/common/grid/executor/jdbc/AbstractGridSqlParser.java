package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.GridFieldDefinition;
import com.bird.service.common.grid.GridFieldType;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.query.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuxx
 * @since 2021/1/20
 */
@Slf4j
@SuppressWarnings("Duplicates")
public abstract class AbstractGridSqlParser implements IGridSqlParser {


    @Override
    public PrepareStateParameter listPaged(GridDefinition gridDefinition, PagedListQuery query) {
        PrepareStateParameter stateParameter = this.select(gridDefinition.getFields());
        stateParameter.append(this.from(gridDefinition)).append(this.where(gridDefinition,query));
        if(StringUtils.isNotBlank(gridDefinition.getAppendSql())){
            stateParameter.append(gridDefinition.getAppendSql());
        }

        String sortField = gridDefinition.getDefaultSortField();
        int sortDirection = gridDefinition.getDefaultSortDirection().getCode();
        if (StringUtils.isNotBlank(query.getSortField())) {
            sortField = query.getSortField();
            sortDirection = query.getSortDirection();
        }
        stateParameter.append(" order by ")
                .append(this.formatDbField(sortField))
                .append(Objects.equals(sortDirection,SortDirectionEnum.DESC.getCode()) ? " desc" : " asc")
                .append(" limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize());

        return stateParameter;
    }

    @Override
    public PrepareStateParameter add(GridDefinition gridDefinition, Map<String, Object> pojo) {
        return null;
    }

    @Override
    public PrepareStateParameter edit(GridDefinition gridDefinition, Map<String, Object> pojo) {
        return null;
    }

    @Override
    public PrepareStateParameter delete(GridDefinition gridDefinition, Object id) {
        return null;
    }

    /**
     * 解析select语句
     *
     * @param fields 字段定义集合
     * @return {@link PrepareStateParameter}
     */
    protected PrepareStateParameter select(Map<String, GridFieldDefinition> fields) {
        StringBuilder builder = new StringBuilder("select ");

        for (Map.Entry<String, GridFieldDefinition> entry : fields.entrySet()) {
            GridFieldDefinition fieldDefinition = entry.getValue();

            builder.append(this.formatDbField(fieldDefinition.getDbField()));
            builder.append(" AS ");
            builder.append(fieldDefinition.getFieldName());
            builder.append(",");
        }

        String select = StringUtils.stripEnd(builder.toString(), ",");
        return new PrepareStateParameter(select);
    }

    /**
     * 解析from语句
     *
     * @param gridDefinition 表格定义
     * @return {@link PrepareStateParameter}
     */
    protected PrepareStateParameter from(GridDefinition gridDefinition) {
        return new PrepareStateParameter(" from " + gridDefinition.getFrom());
    }

    /**
     * 解析where语句
     *
     * @param gridDefinition 表格定义
     * @param query          查询条件
     * @return {@link PrepareStateParameter}
     */
    protected PrepareStateParameter where(GridDefinition gridDefinition, PagedListQuery query) {
        FilterGroup filterGroup = new FilterGroup(query.getFilters());
        PrepareStateParameter queryWhere = this.formatGroup(filterGroup, gridDefinition.getFields());
        PrepareStateParameter fixedWhere = new PrepareStateParameter(gridDefinition.getWhere());

        PrepareStateParameter where;
        if (queryWhere.isEmpty()) {
            where = fixedWhere;
        } else if (fixedWhere.isEmpty()) {
            where = queryWhere;
        } else {
            where = new PrepareStateParameter("(").append(fixedWhere).append(") and ").append(queryWhere);
        }
        if (!where.isEmpty()) {
            where = new PrepareStateParameter(" where ").append(where);
        }
        return where;
    }

    /**
     * 格式化数据库字段名
     *
     * @param dbField 字段名
     * @return 格式化后的字段名
     */
    protected String formatDbField(String dbField) {
        return dbField;
    }

    private PrepareStateParameter formatGroup(FilterGroup filterGroup, Map<String, GridFieldDefinition> fieldMap) {
        PrepareStateParameter where = formatRules(filterGroup.getRules(), fieldMap);

        boolean isStart = true;
        PrepareStateParameter groupWhere = new PrepareStateParameter();
        if (CollectionUtils.isNotEmpty(filterGroup.getGroups())) {
            for (FilterGroup innerGroup : filterGroup.getGroups()) {
                if (innerGroup == null) {
                    continue;
                }
                PrepareStateParameter innerWhere = this.formatGroup(innerGroup, fieldMap);
                if (!innerWhere.isEmpty()) {
                    if (!isStart) {
                        groupWhere.append(" and ");
                    }
                    groupWhere.append(innerWhere);
                    isStart = false;
                }
            }
        }

        if (groupWhere.isEmpty()) {
            return where;
        } else if (where.isEmpty()) {
            return groupWhere;
        } else {
            FilterOperate groupOperate = FilterOperate.resolveOrDefault(filterGroup.getOperate(), FilterOperate.AND);
            if (groupOperate == null) {
                groupOperate = FilterOperate.AND;
            }

            PrepareStateParameter stateParameter = new PrepareStateParameter();
            stateParameter.append("(").append(where).append(" ").append(groupOperate.getDbValue()).append(" ").append(groupWhere).append(")");
            return stateParameter;
        }
    }

    private PrepareStateParameter formatRules(List<FilterRule> rules, Map<String, GridFieldDefinition> fieldMap) {
        PrepareStateParameter stateParameter = new PrepareStateParameter();

        if (CollectionUtils.isEmpty(rules)) {
            return stateParameter;
        }

        boolean isStart = true;
        StringBuilder sb = new StringBuilder();
        for (FilterRule rule : rules) {
            if (rule == null) {
                continue;
            }

            String field = StringUtils.strip(rule.getField());
            String value = StringUtils.strip(rule.getValue());
            if (StringUtils.isBlank(field) || StringUtils.isBlank(value)) {
                continue;
            }
            GridFieldDefinition fieldDefinition = fieldMap.get(field);
            if(fieldDefinition == null || fieldDefinition.getQueryStrategy() == QueryStrategyEnum.FORBID){
                log.warn("表格中字段{}不允许查询",field);
                continue;
            }
            if (!isStart) {
                sb.append(" and ");
            }

            FilterOperate ruleOperate = FilterOperate.resolve(rule.getOperate());
            if (ruleOperate == null) {
                ruleOperate = FilterOperate.EQUAL;
            }

            String dbFieldName = this.getDbFieldName(fieldMap, field);

            if (ruleOperate == FilterOperate.IN) {
                sb.append(String.format("FIND_IN_SET(%s,?)", dbFieldName));
                stateParameter.addParameter(GridFieldType.VARCHAR,StringUtils.strip(value, ","));
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
                stateParameter.addParameter(fieldDefinition.getFieldType(),value);
                sb.append(dbFieldName).append(" ").append(ruleOperate.getDbValue()).append(" ?");
            }
            isStart = false;
        }
        String where = sb.toString();
        if(StringUtils.isNotBlank(where)){
            where = "(" + where + ")";
        }
        stateParameter.setSql(where);
        return stateParameter;
    }

    /**
     * 获取字段对应的数据库列名
     *
     * @param fieldMap map
     * @param field    field
     * @return db field name
     */
    private String getDbFieldName(Map<String, GridFieldDefinition> fieldMap, String field) {
        if (MapUtils.isEmpty(fieldMap)) {
            return field;
        }
        GridFieldDefinition fieldDefinition = fieldMap.get(field);
        String dbField = fieldDefinition == null ? field : fieldDefinition.getDbField();

        return this.formatDbField(dbField);
    }

}
