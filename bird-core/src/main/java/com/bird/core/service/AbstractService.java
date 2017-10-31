package com.bird.core.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.model.AbstractModel;
import com.bird.core.service.query.PagedListResultDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuxx on 2017/7/31.
 */
public interface AbstractService<T extends AbstractModel> {

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param param 筛选条件
     * @return 查询的结果
     */
    PagedListResultDTO queryPagedList(PagedQueryParam param);

    /**
     * 以DTO为根据的通用保存方法
     * param.getEntityDTO().getId()>0 则更新，否则新增
     * @param param
     */
    void save(CommonSaveParam param);

    /**
     * 根据id集合获取数据
     *
     * @param ids id集合
     * @return
     */
    List<T> getList(List<Long> ids);

    /**
     * 根据id集合获取指定类型的数据
     *
     * @param ids id集合
     * @param cls 返回的数据类型
     * @param <K>
     * @return
     */
    <K> List<K> getList(List<Long> ids, Class<K> cls);

    /**
     * 逻辑删除
     *
     * @param id     数据id
     */
    @Transactional
    void softDelete(Long id);

    /**
     * 物理删除
     *
     * @param id 数据id
     */
    @Transactional
    void delete(Long id);

    /**
     * 保存，包括新增与编辑
     *
     * @param record 数据
     * @return
     */
    @Transactional
    T save(T record);

    /**
     * 根据id查询数据并缓存
     *
     * @param id
     * @return
     */
    T queryById(Long id);

    /**
     * 查询符合条件的第一条数据
     *
     * @param entity 查询条件
     * @return
     */
    T selectOne(Wrapper<T> entity);

    /**
     * 查询符合条件的数据集合
     *
     * @param entity 查询条件
     * @return
     */
    List<T> selectList(Wrapper<T> entity);
}
