package com.bird.service.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.IPO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public abstract class AbstractService<M extends AbstractMapper<T>,T extends IPO<TKey>,TKey extends Serializable> extends BaseService implements IService<T,TKey> {

    @Autowired
    protected M mapper;

    /**
     * 根据主键查询实体
     * @param id id
     * @return 实体
     */
    @Override
    public T getById(TKey id) {
        if (isEmptyKey(id)) {
            return null;
        }

        return mapper.selectById(id);
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * 结果集，如果是多个，随机取一条
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public T getOne(Wrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @param throwEx      有多个 result 是否抛出异常
     */
    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx){
        if (throwEx) {
            return mapper.selectOne(queryWrapper);
        }
        List<T> list = mapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                logger.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表
     * @return 实体列表
     */
    @Override
    public List<T> listByIds(Collection<TKey> ids) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            list = mapper.selectBatchIds(ids);
        }
        return list;
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 实体列表
     */
    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 查询结果集
     */
    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return mapper.selectMaps(queryWrapper);
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(mapper.selectCount(queryWrapper));
    }

    /**
     * 校验是否存在（单条件）
     *
     * @param column 列
     * @param value 值
     * @param excludeId 排除的id
     * @return 是否存在
     */
    public boolean checkExist(SFunction<T, ?> column, Object value, Long excludeId) {
        T model = lambdaQuery().eq(column, value).last("limit 1").one();
        if (model == null) {
            return false;
        }

        return !Objects.equals(model.getId(), excludeId);
    }

    /**
     * 根据Model保存数据
     * @param model model
     * @return 保存后的model
     */
    @Override
    public T save(T model) {
        if (isEmptyKey(model.getId())) {
            return this.insert(model);
        } else {
            this.update(model);
            return model;
        }
    }

    /**
     * 新增
     *
     * @param model 数据
     * @return 是否成功
     */
    @Override
    public T insert(T model) {
        mapper.insert(model);
        return model;
    }

    /**
     * 根据Id更新Model
     *
     * @param model model
     * @return 是否成功
     */
    @Override
    public boolean update(T model) {
        int result = mapper.updateById(model);
        return result >= 1;
    }

    /**
     * 根据主键删除数据
     * @param id 主键
     */
    @Override
    public boolean delete(TKey id) {
        return SqlHelper.retBool(mapper.deleteById(id));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类
     */
    @Override
    public boolean delete(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(mapper.delete(queryWrapper));
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param ids 主键ID列表
     */
    @Override
    public boolean deleteByIds(Collection<TKey> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        return SqlHelper.retBool(mapper.deleteBatchIds(ids));
    }

    /**
     * 以下的方法使用介绍:
     *
     * 一. 名称介绍
     * 1. 方法名带有 query 的为对数据的查询操作, 方法名带有 update 的为对数据的修改操作
     * 2. 方法名带有 lambda 的为内部方法入参 column 支持函数式的
     *
     * 二. 支持介绍
     * 1. 方法名带有 query 的支持以 {@link ChainQuery} 内部的方法名结尾进行数据查询操作
     * 2. 方法名带有 update 的支持以 {@link ChainUpdate} 内部的方法名为结尾进行数据修改操作
     *
     * 三. 使用示例,只用不带 lambda 的方法各展示一个例子,其他类推
     * 1. 根据条件获取一条数据: `query().eq("column", value).one()`
     * 2. 根据条件删除一条数据: `update().eq("column", value).remove()`
     *
     */

    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     */
    @Override
    public QueryChainWrapper<T> query() {
        return ChainWrappers.queryChain(mapper);
    }

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        return ChainWrappers.lambdaQueryChain(mapper);
    }

    /**
     * 链式更改 普通
     *
     * @return UpdateWrapper 的包装类
     */
    @Override
    public UpdateChainWrapper<T> update() {
        return ChainWrappers.updateChain(mapper);
    }

    /**
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaUpdateWrapper 的包装类
     */
    @Override
    public LambdaUpdateChainWrapper<T> lambdaUpdate() {
        return ChainWrappers.lambdaUpdateChain(mapper);
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
    protected boolean isEmptyKey(TKey id) {
        return id == null;
    }
}
