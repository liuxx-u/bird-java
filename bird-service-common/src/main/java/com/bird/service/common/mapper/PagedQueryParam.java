package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.query.PagedListQueryDTO;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * @author liuxx
 * @date 2017/10/10
 */
public class PagedQueryParam implements Serializable {

    public PagedQueryParam() {
        this.filterSoftDelete = true;
    }

    public PagedQueryParam(PagedListQueryDTO query, Class<?> tClass) {
        this(query,tClass,null,null,null);
        this.filterSoftDelete = true;
    }

    public PagedQueryParam(PagedListQueryDTO query,String select,String from,String where) {
        this(query, null, select, from, where);
    }

    /**
     * 构造方法
     * @param query 查询条件
     * @param tClass 映射的Class
     * @param select
     * @param from
     * @param where
     */
    public PagedQueryParam(PagedListQueryDTO query,Class<?> tClass,String select,String from,String where) {
        this.query = query;
        this.select = select;
        this.from = from;
        this.where = where;

        this.tClass = tClass;
        this.fieldMap = new HashMap<>();

        initWithClass();
    }

    /**
     * 查询表达式，包括分页、排序和筛选
     */
    private PagedListQueryDTO query;

    /**
     * 查询数据源
     */
    private String from;

    /**
     * 查询字段
     */
    private String select;

    /**
     * 自定义的where条件
     */
    private String where;

    /**
     * 返回数据目标类型
     */
    private Class<?> tClass;

    /**
     * 是否过滤软删除的数据（使用tClass时默认为true）
     */
    private boolean filterSoftDelete;

    /**
     * fieldName与dbFieldName哈希表
     */
    private Map<String,String> fieldMap;

    /**
     * 根据Class名称初始化select与from
     * 如果select不为空，selectSql = select + tClass反射得到的select
     */
    private void initWithClass() {
        if (this.tClass == null) return;


        if (StringUtils.isBlank(this.select)) {
            StringBuilder sb = new StringBuilder();
            List<Field> fields = new ArrayList<>();

            Class tempClass = tClass;
            while (tempClass != null && !tempClass.getName().equals(Object.class.getName())) {
                Field[] tempFields = tempClass.getDeclaredFields();
                fields.addAll(Arrays.asList(tempFields));
                tempClass = tempClass.getSuperclass();
            }

            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) continue;

                String fieldName = field.getName();
                String dbFieldName = tableField == null ? fieldName : tableField.value();
                if (!StringUtils.startsWith(dbFieldName, "`")) {
                    sb.append("`");
                }
                sb.append(dbFieldName);
                if (!StringUtils.endsWith(dbFieldName, "`")) {
                    sb.append("`");
                }

                if (!fieldName.equals(dbFieldName)) {
                    sb.append(" AS ");
                    sb.append(fieldName);
                }

                sb.append(",");
                this.fieldMap.put(fieldName,dbFieldName);
            }

            this.select = StringUtils.stripEnd(sb.toString(), ",");
        }

        //如果from为空，则使用tClass的TableName
        if (StringUtils.isBlank(this.from)) {

            TableName tableName = this.tClass.getAnnotation(TableName.class);
            if (tableName != null) {
                this.from = tableName.value();
            }
        }
    }

    /**
     * 获取通用查询条件
     * @return
     */
    public PagedListQueryDTO getQuery() {
        return query;
    }

    public void setQuery(PagedListQueryDTO query) {
        this.query = query;
    }

    /**
     * 获取查询数据源
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 获取查询字段，默认查询实体包含的所有字段
     *
     * @return
     */
    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    /**
     * 获取自定义where条件
     * @return
     */
    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public boolean isFilterSoftDelete() {
        return filterSoftDelete;
    }

    public void setFilterSoftDelete(boolean filterSoftDelete) {
        this.filterSoftDelete = filterSoftDelete;
    }

    public Map<String,String> getFieldMap() {
        return this.fieldMap;
    }
}
