package com.bird.service.common.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bird.core.exception.AbstractException;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.model.IModel;
import com.bird.service.common.service.dto.AbstractDTO;
import com.bird.service.common.service.dto.EntityDTO;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;

import javax.jws.Oneway;
import java.util.List;

/**
 *
 * @author liuxx
 * @date 2017/7/31
 */
public interface IService<T extends IModel> {

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param param 筛选条件
     * @return 查询的结果
     */
    PagedListResultDTO queryPagedList(PagedQueryParam param) throws AbstractException;

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param queryDTO 筛选条件
     * @param cls      映射的DTO类型
     * @return 查询的结果
     */
    PagedListResultDTO queryPagedList(PagedListQueryDTO queryDTO, Class cls) throws AbstractException;

    /**
     * 以DTO为根据的通用保存方法
     * param.getEntityDTO().getId()>0 则更新，否则新增
     *
     * @param param
     */
    void save(CommonSaveParam param) throws AbstractException;

    /**
     * 以DTO为根据的通用保存方法
     * param.getEntityDTO().getId()>0 则更新，否则新增
     *
     * @param dto 数据
     */
    void save(EntityDTO dto) throws AbstractException;

    /**
     * 定义通用的 获取树数据方法
     *
     * @param param
     * @return
     */
    List<TreeDTO> getTreeData(TreeQueryParam param) throws AbstractException;

    /**
     * 根据id集合获取数据
     *
     * @param ids id集合
     * @return
     */
    List<T> getList(List<Long> ids) throws AbstractException;

    /**
     * 根据id集合获取指定类型的数据
     *
     * @param ids id集合
     * @param cls 返回的数据类型
     * @param <K>
     * @return
     */
    <K> List<K> getList(List<Long> ids, Class<K> cls) throws AbstractException;

    /**
     * 物理删除
     *
     * @param id 数据id
     */
    void delete(Long id) throws AbstractException;

    /**
     * 保存，包括新增与编辑
     *
     * @param record 数据
     * @return
     */
    T save(T record) throws AbstractException;

    /**
     * 根据id查询数据并缓存
     *
     * @param id
     * @return
     */
    T queryById(Long id) throws AbstractException;

    /**
     * 查询符合条件的第一条数据
     *
     * @param entity 查询条件
     * @return
     */
    T selectOne(Wrapper<T> entity) throws AbstractException;

    /**
     * 查询符合条件的数据集合
     *
     * @param entity 查询条件
     * @return
     */
    List<T> selectList(Wrapper<T> entity) throws AbstractException;
}
