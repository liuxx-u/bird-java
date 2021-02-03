package com.bird.service.common.grid.controller;

import com.bird.service.common.grid.executor.GridExecutorFactory;
import com.bird.service.common.service.query.PagedListQuery;
import com.bird.service.common.service.query.PagedResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/22
 */
@RestController
@RequestMapping("/v1/infra/grid")
public class GridController {

    private final GridExecutorFactory executorFactory;

    public GridController(GridExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    /**
     * 列表查询
     *
     * @param gridName 表格名称
     * @param query    查询条件
     * @return 查询结果
     */
    @PostMapping("/{gridName}/listPaged")
    public PagedResult<Map<String, Object>> listPaged(@PathVariable("gridName") String gridName, @RequestBody PagedListQuery query) {
        return executorFactory.listPaged(gridName, query);
    }

    /**
     * 新增
     *
     * @param gridName 表格名称
     * @param model    实体内容
     * @return 主键id
     */
    @PostMapping("/{gridName}/insert")
    public Object insert(@PathVariable("gridName") String gridName, @RequestBody Map<String, Object> model) {
        return executorFactory.insert(gridName, model);
    }

    /**
     * 编辑
     *
     * @param gridName 表格名称
     * @param model    实体内容
     * @return 主键id
     */
    @PostMapping("/{gridName}/update")
    public Object update(@PathVariable("gridName") String gridName, @RequestBody Map<String, Object> model) {
        return executorFactory.update(gridName, model);
    }

    /**
     * 编辑
     *
     * @param gridName 表格名称
     * @param id       主键id
     */
    @PostMapping("/{gridName}/delete")
    public void delete(@PathVariable("gridName") String gridName, String id) {
        executorFactory.delete(gridName, id);
    }
}
