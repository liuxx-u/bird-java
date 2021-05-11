package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.GridFieldDefinition;
import com.bird.service.common.grid.GridFieldType;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import com.bird.service.common.grid.enums.SaveStrategyEnum;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.exception.GridException;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import com.bird.service.common.service.query.PagedListQuery;
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

    private final AutoGridJdbcProperties gridJdbcProperties;

    public AbstractGridSqlParser(AutoGridJdbcProperties gridJdbcProperties) {
        this.gridJdbcProperties = gridJdbcProperties;
    }

    private final static String GROUP_BY = "group by";

    @Override
    public PreparedStateParameter listPaged(GridDefinition gridDefinition, PagedListQuery query) {
        PreparedStateParameter stateParameter = this.select(gridDefinition.getFields());
        stateParameter.append(this.from(gridDefinition)).append(this.where(gridDefinition, query));
        if (StringUtils.isNotBlank(gridDefinition.getAppendSql())) {
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
                .append(Objects.equals(sortDirection, SortDirectionEnum.DESC.getCode()) ? " desc" : " asc")
                .append(" limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize());

        return stateParameter;
    }

    @Override
    public PreparedStateParameter listSum(GridDefinition gridDefinition, PagedListQuery query) {
        PreparedStateParameter stateParameter = sum(gridDefinition, query, true);
        stateParameter.append(this.from(gridDefinition)).append(this.where(gridDefinition, query));
        if (StringUtils.isNotBlank(gridDefinition.getAppendSql())) {
            stateParameter.append(gridDefinition.getAppendSql());
        }

        if (StringUtils.containsIgnoreCase(stateParameter.getSql(), GROUP_BY)) {
            PreparedStateParameter groupStateParameter = this.sum(gridDefinition, query, false);
            groupStateParameter.append(" from (").append(stateParameter).append(") as temp");
            return groupStateParameter;
        }
        return stateParameter;
    }

    @Override
    public PreparedStateParameter add(GridDefinition gridDefinition, Map<String, Object> pojo) {
        PreparedStateParameter stateParameter = new PreparedStateParameter("insert into ")
                .append(gridDefinition.getMainTable())
                .append(" (");

        boolean isStart = true;
        StringBuilder valueSql = new StringBuilder();
        for (Map.Entry<String, GridFieldDefinition> entry : gridDefinition.getFields().entrySet()) {
            GridFieldDefinition fieldDefinition = entry.getValue();
            if (SaveStrategyEnum.isIgnoreInsert(fieldDefinition.getSaveStrategy())) {
                continue;
            }
            if (!isStart) {
                stateParameter.append(",");
                valueSql.append(",");
            }
            stateParameter.append(this.formatDbField(fieldDefinition.getDbField()));
            stateParameter.addParameter(fieldDefinition.getFieldType(), pojo.get(fieldDefinition.getFieldName()));
            valueSql.append("?");
            isStart = false;
        }

        stateParameter.append(") values (").append(valueSql.toString()).append(")");
        return stateParameter;
    }

    @Override
    public PreparedStateParameter edit(GridDefinition gridDefinition, Map<String, Object> pojo) {
        GridFieldDefinition primaryKey = gridDefinition.getPrimaryField();
        if (primaryKey == null) {
            throw new GridException("表格:" + gridDefinition.getName() + "更新失败,主键列未定义");
        }
        Object id = pojo.get(primaryKey.getFieldName());
        if (id == null || StringUtils.isBlank(id.toString())) {
            throw new GridException("表格:" + gridDefinition.getName() + "更新失败,更新的主键值为空");
        }

        Map<String, GridFieldDefinition> fieldMap = gridDefinition.getFields();
        PreparedStateParameter stateParameter = new PreparedStateParameter("update " + gridDefinition.getMainTable() + " set ");

        boolean isStart = true;
        PreparedStateParameter updateParameter = new PreparedStateParameter();
        for (Map.Entry<String, Object> entry : pojo.entrySet()) {
            GridFieldDefinition fieldDefinition = fieldMap.get(entry.getKey());
            if (fieldDefinition == null || Objects.equals(gridDefinition.getPrimaryKey(), fieldDefinition.getFieldName())) {
                continue;
            }
            if (SaveStrategyEnum.isIgnoreUpdate(fieldDefinition.getSaveStrategy())) {
                continue;
            }
            if (Objects.equals(fieldDefinition.getSaveStrategy(), SaveStrategyEnum.UPDATE_NULL_IGNORE) && Objects.isNull(entry.getValue())) {
                continue;
            }

            if (!isStart) {
                updateParameter.append(",");
            }
            updateParameter.append(this.formatDbField(fieldDefinition.getDbField())).append(" = ?");
            updateParameter.addParameter(fieldDefinition.getFieldType(), entry.getValue());
            isStart = false;
        }

        if (updateParameter.isEmpty()) {
            log.info("未更新任何数据");
            return null;
        }

        stateParameter.append(updateParameter).append(" where ").append(this.formatDbField(primaryKey.getDbField())).append(" = ? limit 1");
        stateParameter.addParameter(primaryKey.getFieldType(), id);

        return stateParameter;
    }

    @Override
    public PreparedStateParameter delete(GridDefinition gridDefinition, Object id) {
        GridFieldDefinition primaryKey = gridDefinition.getPrimaryField();
        if (primaryKey == null) {
            throw new GridException("表格:" + gridDefinition.getName() + "删除失败,主键列未定义");
        }
        if (id == null || StringUtils.isBlank(id.toString())) {
            throw new GridException("表格:" + gridDefinition.getName() + "删除失败,删除的主键值为空");
        }

        PreparedStateParameter stateParameter = new PreparedStateParameter("delete from " + gridDefinition.getMainTable() + " where " + this.formatDbField(primaryKey.getDbField()) + " = ? limit 1");
        stateParameter.addParameter(primaryKey.getFieldType(), id);
        return stateParameter;
    }

    @Override
    public PreparedStateParameter logicDelete(GridDefinition gridDefinition, Object id) {
        GridFieldDefinition primaryKey = gridDefinition.getPrimaryField();
        if (primaryKey == null) {
            throw new GridException("表格:" + gridDefinition.getName() + "删除失败,主键列未定义");
        }
        if (id == null || StringUtils.isBlank(id.toString())) {
            throw new GridException("表格:" + gridDefinition.getName() + "删除失败,删除的主键值为空");
        }
        GridFieldDefinition logicDeleteField = gridDefinition.getFieldDefinition(gridDefinition.getLogicDeleteField());
        if (logicDeleteField == null) {
            throw new GridException("表格:" + gridDefinition.getName() + "不支持逻辑删除");
        }
        Object logicDeleteValue = logicDeleteField.getLogicDeleteValue();
        if (logicDeleteValue == null || StringUtils.isBlank(logicDeleteValue.toString())) {
            logicDeleteValue = this.gridJdbcProperties.getLogicDeleteValue();
        }

        PreparedStateParameter stateParameter = new PreparedStateParameter("update " + gridDefinition.getMainTable() + " set " + this.formatDbField(logicDeleteField.getDbField()) + " = ?" + " where " + this.formatDbField(primaryKey.getDbField()) + " = ?");
        stateParameter.addParameter(logicDeleteField.getFieldType(), logicDeleteValue);
        stateParameter.addParameter(primaryKey.getFieldType(), id);
        return stateParameter;
    }

    /**
     * 解析select语句
     *
     * @param fields 字段定义集合
     * @return {@link PreparedStateParameter}
     */
    protected PreparedStateParameter select(Map<String, GridFieldDefinition> fields) {
        StringBuilder builder = new StringBuilder("select ");

        for (Map.Entry<String, GridFieldDefinition> entry : fields.entrySet()) {
            GridFieldDefinition fieldDefinition = entry.getValue();
            if (fieldDefinition.getQueryStrategy() == QueryStrategyEnum.HIDE) {
                continue;
            }

            builder.append(this.formatDbField(fieldDefinition.getDbField()));
            builder.append(" as ");
            builder.append(fieldDefinition.getFieldName());
            builder.append(",");
        }

        String select = StringUtils.stripEnd(builder.toString(), ",");
        return new PreparedStateParameter(select);
    }

    /**
     * 解析from语句
     *
     * @param gridDefinition 表格定义
     * @return {@link PreparedStateParameter}
     */
    protected PreparedStateParameter from(GridDefinition gridDefinition) {
        return new PreparedStateParameter(" from " + gridDefinition.getFrom());
    }

    /**
     * 解析where语句
     *
     * @param gridDefinition 表格定义
     * @param query          查询条件
     * @return {@link PreparedStateParameter}
     */
    protected PreparedStateParameter where(GridDefinition gridDefinition, PagedListQuery query) {
        FilterGroup filterGroup = new FilterGroup(query.getFilters());
        PreparedStateParameter queryWhere = this.formatGroup(filterGroup, gridDefinition.getFields());
        PreparedStateParameter fixedWhere = new PreparedStateParameter(gridDefinition.getWhere());
        PreparedStateParameter logicDeleteWhere = this.filterLogicDelete(gridDefinition);
        if (!logicDeleteWhere.isEmpty()) {
            if (fixedWhere.isEmpty()) {
                fixedWhere = logicDeleteWhere;
            } else {
                fixedWhere = fixedWhere.append(" and ").append(logicDeleteWhere);
            }
        }

        PreparedStateParameter where;
        if (queryWhere.isEmpty()) {
            where = fixedWhere;
        } else if (fixedWhere.isEmpty()) {
            where = queryWhere;
        } else {
            where = new PreparedStateParameter("(").append(fixedWhere).append(") and ").append(queryWhere);
        }
        if (!where.isEmpty()) {
            where = new PreparedStateParameter(" where ").append(where);
        }
        return where;
    }

    /**
     * 解析逻辑删除语句
     *
     * @param gridDefinition 表格定义
     * @return 逻辑删除语句
     */
    protected PreparedStateParameter filterLogicDelete(GridDefinition gridDefinition) {
        PreparedStateParameter stateParameter = new PreparedStateParameter();

        GridFieldDefinition logicDeleteField = gridDefinition.getFieldDefinition(gridDefinition.getLogicDeleteField());
        if (logicDeleteField != null) {
            Object logicNotDeleteValue = logicDeleteField.getLogicNotDeleteValue();
            if (logicNotDeleteValue == null || StringUtils.isBlank(logicNotDeleteValue.toString())) {
                logicNotDeleteValue = this.gridJdbcProperties.getLogicNotDeleteValue();
            }
            stateParameter.append(this.formatDbField(logicDeleteField.getDbField())).append(" = ?");
            stateParameter.addParameter(logicDeleteField.getFieldType(), logicNotDeleteValue);
        }
        return stateParameter;
    }

    /**
     * 格式化数据库字段名
     *
     * @param dbField 字段名
     * @return 格式化后的字段名
     */
    private String formatDbField(String dbField) {
        if (StringUtils.startsWith(dbField, "{") && StringUtils.endsWith(dbField, "}")) {
            dbField = StringUtils.removeStart(dbField, "{");
            dbField = StringUtils.removeEnd(dbField, "}");
            return dbField;
        }

        return this.dbFormatField(dbField);
    }

    /**
     * 数据库字段格式化方式，由子类重写
     * @param dbField 数据库字段名
     * @return 格式化后的数据库字段名
     */
    protected String dbFormatField(String dbField){
        return dbField;
    }

    /**
     * 解析sum语句
     *
     * @param gridDefinition 表格定义
     * @param query          查询条件
     * @param useAlias       是否使用别名
     * @return {@link PreparedStateParameter}
     */
    private PreparedStateParameter sum(GridDefinition gridDefinition, PagedListQuery query, Boolean useAlias) {
        StringBuilder sb = new StringBuilder("select count(1) as totalCount");
        for (String field : query.getSumFields()) {
            field = StringUtils.trim(field);
            GridFieldDefinition fieldDefinition = gridDefinition.getFieldDefinition(field);
            if (field == null || !GridFieldType.isSummable(fieldDefinition.getFieldType())) {
                continue;
            }
            sb.append(",")
                    .append("sum(")
                    .append(useAlias ? this.formatDbField(fieldDefinition.getDbField()) : field)
                    .append(") as ")
                    .append(field);
        }
        return new PreparedStateParameter(sb.toString());
    }

    private PreparedStateParameter formatGroup(FilterGroup filterGroup, Map<String, GridFieldDefinition> fieldMap) {
        PreparedStateParameter where = formatRules(filterGroup.getRules(), fieldMap);

        boolean isStart = true;
        PreparedStateParameter groupWhere = new PreparedStateParameter();
        if (CollectionUtils.isNotEmpty(filterGroup.getGroups())) {
            for (FilterGroup innerGroup : filterGroup.getGroups()) {
                if (innerGroup == null) {
                    continue;
                }
                PreparedStateParameter innerWhere = this.formatGroup(innerGroup, fieldMap);
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

            PreparedStateParameter stateParameter = new PreparedStateParameter();
            stateParameter.append("(").append(where).append(" ").append(groupOperate.getDbValue()).append(" ").append(groupWhere).append(")");
            return stateParameter;
        }
    }

    private PreparedStateParameter formatRules(List<FilterRule> rules, Map<String, GridFieldDefinition> fieldMap) {
        PreparedStateParameter stateParameter = new PreparedStateParameter();

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
            if (fieldDefinition == null || fieldDefinition.getQueryStrategy() == QueryStrategyEnum.FORBID || fieldDefinition.getQueryStrategy() == QueryStrategyEnum.HIDE) {
                log.warn("表格中字段{}不允许查询", field);
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
                stateParameter.addParameter(GridFieldType.VARCHAR, StringUtils.strip(value, ","));
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
                stateParameter.addParameter(fieldDefinition.getFieldType(), value);
                sb.append(dbFieldName).append(" ").append(ruleOperate.getDbValue()).append(" ?");
            }
            isStart = false;
        }
        String where = sb.toString();
        if (StringUtils.isNotBlank(where)) {
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
