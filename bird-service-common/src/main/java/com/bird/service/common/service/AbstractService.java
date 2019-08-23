package com.bird.service.common.service;

import com.bird.core.session.BirdSession;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.IModel;
import org.apache.commons.lang3.StringUtils;

/**
 * 业务逻辑层基类
 * 继承基类后必须配置CacheConfig(cacheNames="")
 *
 *
 * @author liuxx
 * @date 2017/5/12
 */
@Deprecated
public abstract class AbstractService<M extends AbstractMapper<T>,T extends IModel<Long>> extends GenericLongService<M,T> implements IService<T> {

    /**
     * 从线程中获取当前登录用户的tenantId
     *
     * @return
     */
    @Deprecated
    protected Long getTenantId() {
        BirdSession session = getSession();
        if (session == null) return 0L;
        if (session.getTenantId() == null || StringUtils.isBlank(session.getTenantId().toString())) return 0L;
        return Long.valueOf(session.getTenantId().toString());
    }
}
