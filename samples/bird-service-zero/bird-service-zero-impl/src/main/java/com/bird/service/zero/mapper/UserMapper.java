package com.bird.service.zero.mapper;

import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxx on 2017/10/10.
 */
public interface UserMapper extends AbstractMapper<User> {

    /**
     * 根据userName获取用户（支持手机号）
     * @param userName userName
     * @return
     */
    User getUserByName(@Param("userName") String userName);

    /**
     * 设置用户角色
     * @param userRoles
     */
    void setUserRoles(@Param("model") UserRoleDTO userRoles);

    /**
     * 删除用户所有角色
     * @param userId
     */
    void deleteUserRoles(@Param("userId") Long userId);

    /**
     * 获取用户角色id集合
     * @param userId
     * @return
     */
    List<Long> getUserRoleIds(@Param("userId") Long userId);

    /**
     * 获取用户权限集合
     * @param userId
     * @return
     */
    List<String> getUserPermissionNames(@Param("userId") Long userId);
}
