package com.bird.service.common.mapper.injector;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/22
 *
 * 审计字段（createTime,modifiedTime）自动填充处理器
 */
public class AuditMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("delFlag", false, metaObject);
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("modifiedTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifiedTime", new Date(), metaObject);
    }
}
