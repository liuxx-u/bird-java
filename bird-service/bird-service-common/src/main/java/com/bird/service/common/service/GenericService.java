package com.bird.service.common.service;

import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
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
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private volatile Class<T> modelClass;

    private volatile Class<TKey> keyClass;

    @SuppressWarnings("all")
    protected Class<T> getModelClass(){
        if(modelClass == null){
            synchronized (this){
                if(modelClass == null){
                    modelClass = (Class<T>)ReflectionKit.getSuperClassGenricType(getClass(), 1);
                }
            }
        }
        return modelClass;
    }

    @SuppressWarnings("all")
    protected Class<TKey> getKeyClass(){
        if(keyClass == null){
            synchronized (this){
                if(keyClass == null){
                    keyClass = (Class<TKey>)ReflectionKit.getSuperClassGenricType(getClass(), 1);
                }
            }
        }
        return keyClass;
    }

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
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public TKey save(CommonSaveParam<TKey> param) {
        return this.save(param.getEntityDTO());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TKey save(IEntityDTO<TKey> dto) {
        try {
            TKey id = dto.getId();
            if (this.isEmptyKey(id)) {
                return this.insert(dto);
            } else {
                return this.update(dto);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TKey insert(IEntityDTO<TKey> dto) {
        try {
            CommonSaveParam<TKey> param = new CommonSaveParam<>(dto, dto.getClass());
            mapper.insertDto(param);
            return dto.getId();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TKey update(IEntityDTO<TKey> dto){
        try {
            CommonSaveParam<TKey> param = new CommonSaveParam<>(dto, dto.getClass());
            mapper.updateDto(param);
            return dto.getId();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
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
                return this.insert(record);
            } else {
                this.update(record);
                return record;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 新增
     *
     * @param record 数据
     * @return 是否成功
     */
    @Override
    public T insert(T record) {
        try {
            mapper.insert(record);
            return record;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 新增
     *
     * @param record 数据
     * @return 是否成功
     */
    @Override
    public boolean update(T record) {
        try {
            T org = this.queryById(record.getId());
            T update = ClassHelper.getDiff(org, record);
            update.setId(record.getId());
            Integer result = mapper.updateById(update);

            return null != result && result >= 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T queryById(TKey id) {
        if (isEmptyKey(id)) {
            return null;
        }

        return mapper.selectById(id);
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
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(getModelClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod sqlMethod
     * @return String
     */
    private String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(getModelClass()).getSqlStatement(sqlMethod.getMethod());
    }
}
