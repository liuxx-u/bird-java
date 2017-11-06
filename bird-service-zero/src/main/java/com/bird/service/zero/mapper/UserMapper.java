package com.bird.service.zero.mapper;

import com.bird.core.mapper.AbstractMapper;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxx on 2017/10/10.
 */
public interface UserMapper extends AbstractMapper<User> {

    void setUserRoles(@Param("model") UserRoleDTO userRoles);

    void deleteUserRoles(@Param("userId") Long userId);

    List<Long> getUserRoleIds(@Param("userId") Long userId);

    List<String> getUserPermissionNames(@Param("userId") Long userId);
}
