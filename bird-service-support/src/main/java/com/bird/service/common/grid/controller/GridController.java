package com.bird.service.common.grid.controller;

import com.bird.service.common.grid.enums.GridActionEnum;
import com.bird.service.common.grid.executor.GridExecuteContext;
import com.bird.service.common.service.query.PagedListQuery;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/22
 */
@RestController
@RequestMapping("/v1/infra/grid")
public class GridController {

    private final GridExecuteContext executeContext;

    public GridController(GridExecuteContext executeContext) {
        this.executeContext = executeContext;
    }

    /**
     * 列表查询
     *
     * @param gridName 表格名称
     * @param query    查询条件
     * @return 查询结果
     */
    @PostMapping("/{gridName}/listPaged")
    public Object listPaged(@PathVariable("gridName") String gridName, @RequestBody PagedListQuery query) {
        return executeContext.execute(gridName, GridActionEnum.QUERY, query);
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
        return executeContext.execute(gridName, GridActionEnum.INSERT, model);
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
        return executeContext.execute(gridName, GridActionEnum.UPDATE, model);
    }

    /**
     * 编辑
     *
     * @param gridName 表格名称
     * @param id       主键id
     */
    @PostMapping("/{gridName}/delete")
    public void delete(@PathVariable("gridName") String gridName, String id) {
        executeContext.execute(gridName, GridActionEnum.DELETE, id);
    }
}
