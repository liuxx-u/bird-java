package com.bird.web.admin.controller.sys;

import com.bird.core.NameValue;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.RoleService;
import com.bird.service.zero.dto.RoleDTO;
import com.bird.service.zero.dto.RolePermissionDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
@Api(description = "系统-角色接口")
@RestController
@RequestMapping("/sys/role")
public class RoleController extends AbstractController {

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/getPaged")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, RoleDTO.class);
        PagedListResultDTO result = roleService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody RoleDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, RoleDTO.class);
        roleService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/delete")
    public OperationResult delete(Long id) {
        roleService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @PostMapping(value = "/getAllRoleBriefs")
    public OperationResult<List<NameValue>> getAllRoleBriefs() {
        List<NameValue> result = roleService.getAllRoleBriefs();
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/getRolePermissionIds")
    public OperationResult<List<Long>> getRolePermissionIds(Long roleId) {
        List<Long> result = roleService.getRolePermissionIds(roleId);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "saveRolePermissions")
    public OperationResult saveRolePermissions(@RequestBody RolePermissionDTO dto){
        roleService.setRolePermissions(dto);
        return OperationResult.Success("保存成功",null);
    }
}
