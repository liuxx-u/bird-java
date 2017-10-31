package com.bird.service.zero.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bird.core.Check;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.mapper.UserMapper;
import com.bird.service.zero.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/10/11.
 */
@Service
@CacheConfig(cacheNames = "zero_user")
@com.alibaba.dubbo.config.annotation.Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名获取用户
     * @param userName 用户名
     * @return 用户
     */
    @Override
    public User getUserByLoginName(String userName) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("userName = {0} and delFlag = 0",userName);
        return selectOne(ew);
    }

    /**
     * 设置用户角色
     * @param dto 用户角色信息
     */
    @Override
    public void setUserRoles(UserRoleDTO dto) {
        Check.NotNull(dto, "dto");
        Check.GreaterThan(dto.getUserId(), 0L, "dto.userId");

        userMapper.deleteUserRoles(dto.getUserId());
        userMapper.setUserRoles(dto);
    }

    /**
     * 获取用户拥有的所有角色Id
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return userMapper.getUserRoleIds(userId);
    }
}
