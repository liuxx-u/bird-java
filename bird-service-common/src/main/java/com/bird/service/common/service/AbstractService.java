package com.bird.service.common.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bird.core.Constant;
import com.bird.core.cache.CacheHelper;
import com.bird.core.exception.ExceptionHelper;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.DozerHelper;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.model.*;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 业务逻辑层基类
 * 继承基类后必须配置CacheConfig(cacheNames="")
 *
 *
 * @author liuxx
 * @date 2017/5/12
 */
public abstract class AbstractService<M extends AbstractMapper<T>,T extends IModel> implements IService<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected M mapper;

    @Autowired
    protected DozerHelper dozer;

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param param 筛选条件
     * @return 查询的结果
     */
    @Override
    public PagedListResultDTO queryPagedList(PagedQueryParam param) {
        Long totalCount = mapper.queryTotalCount(param);
        List<Map> items = new ArrayList<>();
        if (totalCount > 0) {
            items = mapper.queryPagedList(param);
        }
        return new PagedListResultDTO(totalCount, items);
    }

    /**
     * 以DTO为根据的通用保存方法
     * param.getEntityDTO().getId()>0 则更新，否则新增
     *
     * @param param
     */
    @Override
    @Transactional
    public void save(CommonSaveParam param) {
        Long id = param.getEntityDTO().getId();
        if (id == null || id <= 0) {
            mapper.insertDto(param);
        } else {
            String lockKey = getLockKey(id);
            if (CacheHelper.getLock(lockKey)) {
                try {
                    mapper.updateDto(param);
                    CacheHelper.getCache().del(getCacheKey(id));
                } finally {
                    CacheHelper.unlock(lockKey);
                }
            } else {
                sleep(20);
                save(param);
            }
        }
    }

    /**
     * 通用的获取树数据方法
     *
     * @param param
     * @return
     */
    @Override
    public List<TreeDTO> getTreeData(TreeQueryParam param) {
        return mapper.queryTreeData(param);
    }

    /**
     * 根据id集合获取数据
     *
     * @param ids id集合
     */
    @Override
    public List<T> getList(List<Long> ids) {
        List<T> list = new ArrayList<>();
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                list.add(null);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i = 0; i < ids.size(); i++) {
                final int index = i;
                executorService.execute(() -> list.set(index, queryById(ids.get(index))));
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                logger.error("awaitTermination", "", e);
            }
        }
        return list;
    }

    /**
     * 根据id集合获取指定类型的数据
     *
     * @param ids id集合
     * @param cls 返回的数据类型
     */
    @Override
    public <K> List<K> getList(List<Long> ids, Class<K> cls) {
        List<K> list = new ArrayList<>();
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                list.add(null);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i = 0; i < ids.size(); i++) {
                final int index = i;
                executorService.execute(() -> {
                    T t = queryById(ids.get(index));
                    K k = dozer.map(t, cls);
                    list.set(index, k);
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                logger.error("awaitTermination", "", e);
            }
        }
        return list;
    }

    /**
     * 物理删除
     *
     * @param id 数据id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        try {
            mapper.deleteById(id);
            CacheHelper.getCache().del(getCacheKey(id));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 保存，包括新增与编辑
     *
     * @param record 数据
     */
    @Override
    @Transactional
    public T save(T record) {
        try {
            if (record.getId() == null || record.getId() == 0) {
                mapper.insert(record);
            } else {
                T org = this.queryById(record.getId());
                String lockKey = getLockKey(record.getId());
                if (CacheHelper.getLock(lockKey)) {
                    try {
                        T update = ClassHelper.getDiff(org, record);
                        update.setId(record.getId());
                        mapper.updateById(update);
                        record = mapper.selectById(record.getId());
                        CacheHelper.getCache().set(getCacheKey(record.getId()), record);
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
            logger.error(Constant.Exception_Head + msg, e);
            throw new RuntimeException("已经存在相同的配置.");
        } catch (Exception e) {
            String msg = ExceptionHelper.getStackTraceAsString(e);
            logger.error(Constant.Exception_Head + msg, e);
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
     * 根据id查询数据并缓存
     *
     * @param id
     */
    @Override
    @Transactional
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
     * 查询符合条件的第一条数据
     *
     * @param entity 查询条件
     */
    @Override
    public T selectOne(Wrapper<T> entity) {
        List<T> list = mapper.selectList(entity);
        if (list.size() == 0) return null;
        return list.get(0);
    }

    /**
     * 查询符合条件的数据集合
     *
     * @param entity 查询条件
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
        return new StringBuilder(Constant.Cache.NAMESPACE).append(cacheName).append(":").append(id).toString();
    }

    /**
     * 获取缓存键值
     */
    protected String getLockKey(Object id) {
        String cacheName = getCacheKey();
        return new StringBuilder(Constant.Cache.NAMESPACE).append(cacheName).append(":LOCK:").append(id).toString();
    }

    /**
     * @return
     */
    private String getCacheKey() {
        Class<?> cls = getClass();
        String cacheName = Constant.Cache.ClassKeyMap.get(cls);
        if (StringUtils.isBlank(cacheName)) {
            CacheConfig cacheConfig = cls.getAnnotation(CacheConfig.class);
            if (cacheConfig == null || ArrayUtils.isEmpty(cacheConfig.cacheNames())) {
                cacheName = getClass().getName();
            } else {
                cacheName = cacheConfig.cacheNames()[0];
            }
            Constant.Cache.ClassKeyMap.put(cls, cacheName);
        }
        return cacheName;
    }
}
