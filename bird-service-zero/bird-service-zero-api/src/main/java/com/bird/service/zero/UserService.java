package com.bird.service.zero;

import com.bird.core.sso.LoginDTO;
import com.bird.core.sso.LoginResult;
import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.model.User;

import java.util.List;

/**
 * Created by liuxx on 2017/10/10.
 */

public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户
     * @param userName 用户名
     * @return 用户
     */
    User getUserByLoginName(String userName);

    /**
     * 设置用户角色
     * @param dto 用户角色信息
     */
    void setUserRoles(UserRoleDTO dto);

    /**
     * 获取用户拥有的所有角色Id
     * @param userId 用户id
     * @return
     */
    List<Long> getUserRoleIds(Long userId);

    /**
     * 获取用户拥有的权限集合
     * @param userId 用户id
     * @return
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 用户登录
     * @return 登录结果
     */
    LoginResult Login(LoginDTO loginDTO);
}
