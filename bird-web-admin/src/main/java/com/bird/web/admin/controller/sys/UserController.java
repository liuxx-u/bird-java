package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserDTO;
import com.bird.service.zero.dto.UserRoleDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
@Api(description = "系统-用户接口")
@RestController
@RequestMapping("/sys/user")
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getPaged", method = {RequestMethod.POST})
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, UserDTO.class);
        PagedListResultDTO result = userService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public OperationResult save(@RequestBody UserDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, UserDTO.class);
        userService.save(param);

        return OperationResult.Success("保存成功",null);
    }

    @RequestMapping(value="/delete",method = RequestMethod.POST)
    public OperationResult delete(Long id){

        Check.GreaterThan(id,0L,"id");
        userService.softDelete(id);

        return OperationResult.Success("删除成功",null);
    }

    @RequestMapping(value = "/setUserRoles", method = {RequestMethod.POST})
    public OperationResult setUserRoles(@RequestBody UserRoleDTO dto) {

        Check.GreaterThan(dto.getUserId(),0L,"dto.userId");
        userService.setUserRoles(dto);

        return OperationResult.Success("保存成功",null);
    }

    @RequestMapping(value = "/getUserRoleIds",method = {RequestMethod.POST})
    public OperationResult<List<Long>> getUserRoleIds(Long userId) {

        Check.GreaterThan(userId,0L,"userId");
        List<Long> result = userService.getUserRoleIds(userId);
        return OperationResult.Success("获取成功", result);
    }


    public List<String> getUserPermissions(){
        return null;
    }

    public void getUserMenus(){}
}
