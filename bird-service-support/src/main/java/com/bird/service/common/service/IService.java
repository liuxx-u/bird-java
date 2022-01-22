package com.bird.service.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.bird.service.common.model.IPO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public interface IService<T extends IPO<TKey>,TKey extends Serializable> {

    /**
     * 根据主键查询实体
     * @param id id
     * @return 实体
     */
    T getById(TKey id);

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     *
     * @param queryWrapper 实体对象封装操作类
     */
    T getOne(Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @param throwEx      有多个 result 是否抛出异常
     */
    T getOne(Wrapper<T> queryWrapper, boolean throwEx);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表
     * @return 实体列表
     */
    List<T> listByIds(Collection<TKey> ids);

    /**
     * 查询（根据ID 批量查询）并转换为指定的数据类型
     *
     * @param ids 主键ID列表
     * @return 指定类型的数据列表
     */
    <K> List<K> listByIds(Collection<TKey> ids, Class<K> cls);

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 实体列表
     */
    List<T> list(Wrapper<T> queryWrapper);

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 查询结果集
     */
    List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类
     */
    int count(Wrapper<T> queryWrapper);

    /**
     * 根据Model保存数据
     * @param model model
     * @return 保存后的model
     */
    T save(T model);

    /**
     * 新增
     *
     * @param model 数据
     * @return 是否成功
     */
    T insert(T model);

    /**
     * 根据Id更新Model
     *
     * @param model model
     * @return 是否成功
     */
    boolean update(T model);

    /**
     * 根据主键删除数据
     * @param id 主键
     * @return 是否删除成功
     */
    boolean delete(TKey id);

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类
     */
    boolean delete(Wrapper<T> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param ids 主键ID列表
     */
    boolean deleteByIds(Collection<TKey> ids);

    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     */
    QueryChainWrapper<T> query();

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    LambdaQueryChainWrapper<T> lambdaQuery();

    /**
     * 链式更改 普通
     *
     * @return UpdateWrapper 的包装类
     */
    UpdateChainWrapper<T> update();

    /**
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaUpdateWrapper 的包装类
     */
    LambdaUpdateChainWrapper<T> lambdaUpdate();

}
