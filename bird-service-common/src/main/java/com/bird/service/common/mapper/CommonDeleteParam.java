package com.bird.service.common.mapper;

import java.io.Serializable;

/**
 * Created by liuxx on 2017/11/2.
 */
public class CommonDeleteParam implements Serializable {
    private String tableName;
    private String where;
    private boolean softDelete;

    public CommonDeleteParam() {
        this.softDelete = true;
    }

    public CommonDeleteParam(String tableName,String where) {
        this(tableName,where,true);
    }

    public CommonDeleteParam(String tableName,String where,boolean softDelete) {
        this.tableName = tableName;
        this.where = where;
        this.softDelete = softDelete;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }
}
