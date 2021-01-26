package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bird.service.common.model.IDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @author liuxx
 * @date 2017/5/15
 */
public interface AbstractMapper<T extends IDO> extends BaseMapper<T> {

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param param 筛选条件
     * @return 查询的结果
     */
    @Deprecated
    @SelectProvider(type = PagedQueryProvider.class, method = "queryPagedList")
    List<Map> queryPagedList(PagedQueryParam param);

    /**
     * 查询 通用查询中的合计信息
     * @param param 筛选条件
     * @return 合计信息
     */
    @Deprecated
    @SelectProvider(type = PagedQueryProvider.class, method = "queryPagedSum")
    Map<String,Number> queryPagedSum(PagedQueryParam param);

    /**
     * 通用新增方法
     *
     * @param param
     */
    @Deprecated
    @InsertProvider(type = CommonSaveProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "entityDTO.id")
    void insertDto(CommonSaveParam param);

    /**
     * 通用更新方法
     *
     * @param param 保存参数
     */
    @Deprecated
    @UpdateProvider(type = CommonSaveProvider.class, method = "update")
    void updateDto(CommonSaveParam param);
}
