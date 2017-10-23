package com.bird.core.mapper;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.utils.StringHelper;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuxx on 2017/10/10.
 */
public class PagedQueryParam implements Serializable {

    public PagedQueryParam() {
    }

    public PagedQueryParam(PagedListQueryDTO query, Class<?> tClass) {
        this.query = query;
        this.tClass = tClass;
        initWithClass();
    }

    public PagedQueryParam(PagedListQueryDTO query, Class<?> tClass, String from, String select) {
        this.query = query;
        this.tClass = tClass;
        this.from = from;
        this.select = select;
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
     * 返回数据目标类型
     */
    private Class<?> tClass;

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

    /**
     * 获取查询字段，默认查询实体包含的所有字段
     *
     * @return
     */
    public String getSelect() {
        return select;
    }


    private void initWithClass() {
        if (StringHelper.isNullOrWhiteSpace(this.select)) {
            StringBuilder sb = new StringBuilder();
            List<Field> fields = new ArrayList<>();

            Class tempClass = tClass;
            while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
                Field[] tempFields = tempClass.getDeclaredFields();
                fields.addAll(Arrays.asList(tempFields));
                tempClass = tempClass.getSuperclass();
            }

            for (Field field : fields) {
                sb.append(field.getName() + ",");
            }
            this.select = StringHelper.trimEnd(sb.toString(), ',');
        }

        if (StringHelper.isNullOrWhiteSpace(this.from)) {

            TableName tableName = this.tClass.getAnnotation(TableName.class);
            if (tableName != null) {
                this.from = tableName.value();
            }
        }
    }
}
