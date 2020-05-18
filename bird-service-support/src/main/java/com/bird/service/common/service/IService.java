package com.bird.service.common.service;

import com.bird.service.common.model.IModel;
import com.bird.service.common.service.dto.IEntityDTO;
import com.bird.service.common.service.query.PagedListQuery;
import com.bird.service.common.service.query.PagedListResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public interface IService<T extends IModel<TKey>,TKey extends Serializable> {


    /**
     * 定义通用的单表查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param queryDTO 筛选条件
     * @param cls      映射的DTO类型
     * @return 查询的结果
     */
    PagedListResult queryPagedList(PagedListQuery queryDTO, Class cls);

    /**
     * 以DTO为根据的通用保存方法
     * @param dto dto
     * @return 保存后的主键
     */
    TKey save(IEntityDTO<TKey> dto);

    /**
     * 以DTO为根据 新增
     * @param dto 数据
     * @return id
     */
    TKey insert(IEntityDTO<TKey> dto);

    /**
     * 以DTO为根据 编辑
     * @param dto 数据
     * @return id
     */
    TKey update(IEntityDTO<TKey> dto);

    /**
     * 根据id集合获取数据
     *
     * @param ids id集合
     * @return
     */
    List<T> getList(Collection<TKey> ids);

    /**
     * 根据id集合获取指定类型的数据
     *
     * @param ids id集合
     * @param cls 返回的数据类型
     * @param <K> K
     * @return list<K>
     */
    <K> List<K> getList(Collection<TKey> ids, Class<K> cls);

    /**
     * 物理删除
     *
     * @param id 数据id
     */
    void delete(TKey id);

    /**
     * 保存，包括新增与编辑
     *
     * @param record 数据
     * @return model
     */
    T save(T record);

    /**
     * 新增
     * @param record 数据
     * @return 是否成功
     */
    T insert(T record);


    /**
     * 编辑
     * @param record 数据
     * @return 是否成功
     */
    boolean update(T record);

    /**
     * 根据id查询数据并缓存
     *
     * @param id id
     * @return model
     */
    T queryById(TKey id);

    /**
     * <p>
     * 插入（批量），该方法不适合 Oracle
     * </p>
     *
     * @param entityList 实体对象列表
     * @return boolean
     */
    default boolean insertBatch(Collection<T> entityList){
        return insertBatch(entityList,500);
    }

    /**
     * <p>
     * 插入（批量）
     * </p>
     *
     * @param entityList 实体对象列表
     * @param batchSize  插入批次数量
     * @return boolean
     */
    boolean insertBatch(Collection<T> entityList, int batchSize);


}
