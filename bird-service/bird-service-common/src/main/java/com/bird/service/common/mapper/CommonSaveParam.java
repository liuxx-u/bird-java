package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.exception.UserFriendlyException;
import com.bird.service.common.service.dto.IEntityDTO;

import java.io.Serializable;

/**
 *
 * @author liuxx
 * @date 2017/10/20
 */
public class CommonSaveParam<TKey extends Serializable> implements Serializable {
    public CommonSaveParam(IEntityDTO<TKey> entityDTO, Class<?> tClass) {
        this.entityDTO = entityDTO;
        this.tClass = tClass;
    }

    /**
     * 目标数据类型
     */
    private Class<?> tClass;

    private IEntityDTO<TKey> entityDTO;

    public IEntityDTO<TKey> getEntityDTO() {
        return entityDTO;
    }

    public void setEntityDTO(IEntityDTO<TKey> entityDTO) {
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
