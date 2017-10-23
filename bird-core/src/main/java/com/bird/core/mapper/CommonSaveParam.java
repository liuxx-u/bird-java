package com.bird.core.mapper;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.exception.UserFriendlyException;
import com.bird.core.service.EntityDTO;

import java.io.Serializable;

/**
 * Created by liuxx on 2017/10/20.
 */
public class CommonSaveParam implements Serializable {
    public CommonSaveParam(EntityDTO entityDTO, Class<?> tClass) {
        this.entityDTO = entityDTO;
        this.tClass = tClass;
    }

    /**
     * 目标数据类型
     */
    private Class<?> tClass;

    private EntityDTO entityDTO;

    public EntityDTO getEntityDTO() {
        return entityDTO;
    }

    public void setEntityDTO(EntityDTO entityDTO) {
        this.entityDTO = entityDTO;
    }


    public String getTableName() {
        TableName tableName = this.tClass.getAnnotation(TableName.class);
        if (tableName == null) {
            throw new UserFriendlyException(this.tClass.getName() + "未定义表名com.baomidou.mybatisplus.annotations.TableName");
        }
        return tableName.value();
    }

    public Class<?> gettClass() {
        return this.tClass;
    }
}
