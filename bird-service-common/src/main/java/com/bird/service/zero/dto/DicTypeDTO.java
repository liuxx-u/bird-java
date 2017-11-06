package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.service.EntityDTO;

/**
 * Created by liuxx on 2017/11/2.
 */
@TableName("zero_dicType")
public class DicTypeDTO extends EntityDTO {
    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
