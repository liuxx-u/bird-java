package com.bird.service.common.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bird.core.Constant;
import com.bird.core.cache.CacheHelper;
import com.bird.core.exception.ExceptionHelper;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.DozerHelper;
import com.bird.service.common.exception.RollbackException;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.model.IModel;
import com.bird.service.common.service.dto.EntityDTO;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 业务逻辑层基类
 * 继承基类后必须配置CacheConfig(cacheNames="")
 *
 *
 * @author liuxx
 * @date 2017/5/12
 */
@SuppressWarnings("all")
public abstract class AbstractService<M extends AbstractMapper<T>,T extends IModel> extends AbstractPureService implements IService<T> {

    @Autowired
    protected M mapper;

    @Autowired
    protected DozerHelper dozer;

    @Value("${spring.application.name:bird}")
    private String cachePrefix;

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
    public PagedListResultDTO queryPagedList(PagedListQueryDTO queryDTO, Class cls){
        PagedQueryParam param = new PagedQueryParam(queryDTO,cls);
        return this.queryPagedList(param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public Long save(CommonSaveParam param) {
        Long id = param.getEntityDTO().getId();
        if (id == null || id <= 0) {
            mapper.insertDto(param);
            return param.getEntityDTO().getId();
        } else {
            String lockKey = getLockKey(id);
            if (CacheHelper.getLock(lockKey)) {
                try {
                    CacheHelper.getCache().del(getCacheKey(id));
                    mapper.updateDto(param);
                } finally {
                    CacheHelper.unlock(lockKey);
                    return id;
                }
            } else {
                sleep(20);
                return save(param);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long save(EntityDTO dto){
        CommonSaveParam param = new CommonSaveParam(dto,dto.getClass());
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
    public List<T> getList(List<Long> ids) {
        List<T> list = new ArrayList<>();
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                list.add(null);
            }
            ExecutorService poolExecutor = new ScheduledThreadPoolExecutor(10,new BasicThreadFactory.Builder().build());
            for (int i = 0; i < ids.size(); i++) {
                final int index = i;
                poolExecutor.execute(() -> list.set(index, queryById(ids.get(index))));
            }
            poolExecutor.shutdown();
            try {
                poolExecutor.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.error("awaitTermination", "", e);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <K> List<K> getList(List<Long> ids, Class<K> cls) {
        List<K> list = new ArrayList<>();
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                list.add(null);
            }
            ExecutorService poolExecutor = new ScheduledThreadPoolExecutor(10,new BasicThreadFactory.Builder().build());
            for (int i = 0; i < ids.size(); i++) {
                final int index = i;
                poolExecutor.execute(() -> {
                    T t = queryById(ids.get(index));
                    K k = dozer.map(t, cls);
                    list.set(index, k);
                });
            }
            poolExecutor.shutdown();
            try {
                poolExecutor.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.error("awaitTermination", "", e);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void delete(Long id) {
        try {
            mapper.deleteById(id);
            CacheHelper.getCache().del(getCacheKey(id));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public T save(T record) {
        try {
            if (record.getId() == null || record.getId() == 0) {
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
            throw new RuntimeException("已经存在相同的配置.");
        } catch (Exception e) {
            String msg = ExceptionHelper.getStackTraceAsString(e);
            logger.error(msg, e);
            throw new RuntimeException(msg);
        }
        return record;
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(RandomUtils.nextLong(10, millis));
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T queryById(Long id) {
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
                logger.debug(getClass().getSimpleName() + ":" + id + " retry queryById.");
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
     * 获取缓存键值
     */
    protected String getCacheKey(Object id) {
        String cacheName = getCacheKey();
        return new StringBuilder(cachePrefix).append(":").append(cacheName).append(":").append(id).toString();
    }

    /**
     * 获取缓存键值
     */
    protected String getLockKey(Object id) {
        String cacheName = getCacheKey();
        return new StringBuilder(cachePrefix).append(":").append(cacheName).append(":LOCK:").append(id).toString();
    }

    /**
     * @return
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
}
