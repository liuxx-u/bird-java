package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.zero.PermissionService;
import com.bird.service.zero.dto.PermissionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "系统-权限接口")
@RestController
@RequestMapping("/sys/permission")
public class PermissionController extends AbstractController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/getTreeData")
    @ApiOperation(value = "获取权限树数据")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`","`name`","`parentId`");
        param.setFrom("`zero_permission`");
        List<TreeDTO> result = permissionService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/get")
    @ApiOperation(value = "获取权限")
    public OperationResult<PermissionDTO> getPermission(Long id) {
        Check.GreaterThan(id, 0L, "id");
        PermissionDTO result = permissionService.getPermission(id);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存权限")
    public OperationResult save(@RequestBody PermissionDTO dto){
        permissionService.savePermission(dto);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除权限")
    public OperationResult delete(Long id) {
        permissionService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }
}
