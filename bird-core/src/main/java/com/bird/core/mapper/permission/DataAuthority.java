package com.bird.core.mapper.permission;

import com.bird.core.Constants;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/1/25
 */
public class DataAuthority implements Serializable {

    private Long userId;
    private String field;

    public DataAuthority() {
        field = Constants.DataAuthority.DEFAULT_AUTHORITY_FIELD;
    }

    public DataAuthority(Long userId) {
        this();
        this.userId = userId;
    }

    public DataAuthority(Long userId,String field) {
        this.userId = userId;
        this.field = field;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
