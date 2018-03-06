package com.bird.service.scheduler.mapper;

import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.PagedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface SchedulerMapper {

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
     * 判断任务是否存在
     * @param jobName
     * @return
     */
    boolean isExist(@Param("jobName") String jobName);

    /**
     * 删除任务
     * @param jobName
     */
    void removeJob(String jobName);
}
