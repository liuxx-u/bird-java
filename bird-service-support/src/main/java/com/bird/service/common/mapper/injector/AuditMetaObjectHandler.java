package com.bird.service.common.mapper.injector;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bird.core.session.SessionContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/22
 *
 * 审计字段（createTime,modifiedTime）自动填充处理器
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("delFlag", false, metaObject);
        setFieldValByName("creatorId", SessionContext.getUserId(), metaObject);
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("modifiedTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifierId", SessionContext.getUserId(), metaObject);
        setFieldValByName("modifiedTime", new Date(), metaObject);
    }
}
