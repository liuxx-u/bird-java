package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.model.User;

import java.util.List;

/**
 * Created by liuxx on 2017/10/10.
 */

public interface UserService extends AbstractService<User> {

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
}
