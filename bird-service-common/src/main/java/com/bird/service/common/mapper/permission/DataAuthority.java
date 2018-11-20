package com.bird.service.common.mapper.permission;

import com.bird.service.common.ServiceConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/1/25
 */
@Getter
@Setter
public class DataAuthority implements Serializable {

    private Long userId;
    private String field;

    public DataAuthority() {
        field = ServiceConstant.DataPermission.DEFAULT_AUTHORITY_FIELD;
    }

    public DataAuthority(Long userId) {
        this();
        this.userId = userId;
    }

    public DataAuthority(Long userId,String field) {
        this.userId = userId;
        this.field = field;
    }
}
