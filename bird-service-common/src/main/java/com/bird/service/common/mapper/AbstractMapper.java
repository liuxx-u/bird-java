package com.bird.service.common.mapper;

import com.bird.service.common.model.IModel;
import com.bird.service.common.service.dto.TreeDTO;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 *
 * @author liuxx
 * @date 2017/5/15
 */
public interface AbstractMapper<T extends IModel> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

    /**
     * 定义通用的查询接口（支持查询、分页、排序）
     * 支持灵活组装查询数据源
     * 支持灵活控制返回的字段
     *
     * @param param 筛选条件
     * @return 查询的结果
     */
    @SelectProvider(type = PagedQueryProvider.class, method = "queryPagedList")
    List<Map> queryPagedList(PagedQueryParam param);

    /**
     * 查询通用查询的中数量
     *
     * @param param 筛选条件
     * @return
     */
    @SelectProvider(type = PagedQueryProvider.class, method = "queryTotalCount")
    Long queryTotalCount(PagedQueryParam param);

    /**
     * 通用新增方法
     *
     * @param param
     */
    @InsertProvider(type = CommonSaveProvider.class, method = "insert")
    void insertDto(CommonSaveParam param);

    /**
     * 通用更新方法
     *
     * @param param
     */
    @UpdateProvider(type = CommonSaveProvider.class, method = "update")
    void updateDto(CommonSaveParam param);

    /**
     * 通用删除方法
     *
     * @param param
     */
    @DeleteProvider(type = CommonDeleteProvider.class, method = "delete")
    void deleteDto(CommonDeleteParam param);

    /**
     * 通用获取树数据方法
     *
     *@param param
     * @return
     */
    @SelectProvider(type = TreeQueryProvider.class, method = "queryTreeData")
    List<TreeDTO> queryTreeData(TreeQueryParam param);
}
