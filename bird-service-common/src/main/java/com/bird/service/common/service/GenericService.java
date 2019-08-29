package com.bird.service.common.service;

import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.bird.core.Constant;
import com.bird.core.cache.CacheHelper;
import com.bird.core.exception.ExceptionHelper;
import com.bird.core.utils.ClassHelper;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.model.IModel;
import com.bird.service.common.service.dto.IEntityDTO;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public abstract class GenericService<M extends AbstractMapper<T>,T extends IModel<TKey>,TKey extends Serializable> extends PureService implements IGenericService<T,TKey> {

    @Autowired
    protected M mapper;

    @Value("${spring.application.name:bird}")
    private String cachePrefix;

    /**
     * 主键是否为空
     * <p>
     * 子类需根据主键类型重写该方法
     * 比如：Long类型的主键判断大于0;String类型的主键判断不为空字符串
     *
     * @param id id
     * @return true or false
     */
    protected Boolean isEmptyKey(TKey id) {
        return id == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedListResultDTO queryPagedList(PagedQueryParam param) {
        Map<String, Number> sum = mapper.queryPagedSum(param);
        Long totalCount = sum.getOrDefault("totalCount", 0L).longValue();
        List<Map> items = new ArrayList<>();
        if (totalCount > 0) {
            items = mapper.queryPagedList(param);

        }
        return new PagedListResultDTO(totalCount, items, sum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedListResultDTO queryPagedList(PagedListQueryDTO queryDTO, Class cls) {
        PagedQueryParam param = new PagedQueryParam(queryDTO, cls);
        return this.queryPagedList(param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TKey save(CommonSaveParam<TKey> param) {
        TKey id = param.getEntityDTO().getId();
        if (isEmptyKey(id)) {
            mapper.insertDto(param);
            return param.getEntityDTO().getId();
        } else {
            String lockKey = getLockKey(id);
            if (CacheHelper.getLock(lockKey)) {
                try {
                    CacheHelper.getCache().del(getCacheKey(id));
                    mapper.updateDto(param);
                    return id;
                } finally {
                    CacheHelper.unlock(lockKey);
                }
            } else {
                sleep(500);
                return save(param);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TKey save(IEntityDTO<TKey> dto) {
        CommonSaveParam<TKey> param = new CommonSaveParam<>(dto, dto.getClass());
        return this.save(param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TreeDTO> getTreeData(TreeQueryParam param) {
        return mapper.queryTreeData(param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getList(List<TKey> ids) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            list = mapper.selectBatchIds(ids);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <K> List<K> getList(List<TKey> ids, Class<K> cls) {
        List<T> list = getList(ids);
        List<K> list2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (T t : list) {
                try {
                    K k = cls.newInstance();
                    BeanUtils.copyProperties(t, k);
                    list2.add(k);
                } catch (Exception e) {
                    logger.error("类型转换错误", e);
                    return list2;
                }
            }
        }
        return list2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TKey id) {
        try {
            mapper.deleteById(id);
            CacheHelper.getCache().del(getCacheKey(id));
        } catch (Exception e) {
            logger.error("数据删除失败", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T record) {
        try {
            if (isEmptyKey(record.getId())) {
                mapper.insert(record);
            } else {
                T org = this.queryById(record.getId());
                String lockKey = getLockKey(record.getId());
                if (CacheHelper.getLock(lockKey)) {
                    try {
                        CacheHelper.getCache().del(getCacheKey(record.getId()));

                        T update = ClassHelper.getDiff(org, record);
                        update.setId(record.getId());
                        mapper.updateById(update);
                    } finally {
                        CacheHelper.unlock(lockKey);
                    }
                } else {
                    sleep(20);
                    return save(record);
                }
            }
        } catch (DuplicateKeyException e) {
            String msg = ExceptionHelper.getStackTraceAsString(e);
            logger.error(Constant.EXCEPTION_HEAD + msg, e);
        } catch (Exception e) {
            String msg = ExceptionHelper.getStackTraceAsString(e);
            logger.error(msg, e);
        }
        return record;
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(RandomUtils.nextLong(10, millis));
        } catch (Exception e) {
            logger.error("thread sleep error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T queryById(TKey id) {
        if (isEmptyKey(id)) {
            return null;
        }

        String key = getCacheKey(id);
        T record = (T) CacheHelper.getCache().get(key);
        if (record == null) {
            String lockKey = getLockKey(id);
            if (CacheHelper.getLock(lockKey)) {
                try {
                    record = mapper.selectById(id);
                    CacheHelper.getCache().set(key, record);
                } finally {
                    CacheHelper.unlock(lockKey);
                }
            } else {
                logger.debug("{}:{} retry queryById.", getClass().getSimpleName(), id);
                sleep(20);
                return queryById(id);
            }
        }
        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T selectOne(Wrapper<T> entity) {
        List<T> list = mapper.selectList(entity);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> selectList(Wrapper<T> entity) {
        return mapper.selectList(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isNotEmpty(entityList)) {
            try(SqlSession batchSqlSession = sqlSessionBatch()) {
                int size = entityList.size();
                String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
                for (int i = 0; i < size; i++) {
                    batchSqlSession.insert(sqlStatement, entityList.get(i));
                    if (i >= 1 && i % batchSize == 0) {
                        batchSqlSession.flushStatements();
                    }
                }
                batchSqlSession.flushStatements();
            }
        }

        return true;
    }

    /**
     * 获取缓存键值
     */
    protected String getCacheKey(Serializable id) {
        String cacheName = getCacheKey();
        return cachePrefix + ":" + cacheName + ":" + id;
    }

    /**
     * 获取缓存键值
     */
    protected String getLockKey(Serializable id) {
        String cacheName = getCacheKey();
        return cachePrefix + ":" + cacheName + ":LOCK:" + id;
    }

    /**
     * @return cacheKey
     */
    private String getCacheKey() {
        Class<?> cls = getClass();
        String cacheName = Constant.Cache.CLASSKEY_MAP.get(cls);
        if (StringUtils.isBlank(cacheName)) {
            CacheConfig cacheConfig = cls.getAnnotation(CacheConfig.class);
            if (cacheConfig == null || ArrayUtils.isEmpty(cacheConfig.cacheNames())) {
                cacheName = getClass().getName();
            } else {
                cacheName = cacheConfig.cacheNames()[0];
            }
            Constant.Cache.CLASSKEY_MAP.put(cls, cacheName);
        }
        return cacheName;
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(getClass(), 1);
    }

    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }
}
