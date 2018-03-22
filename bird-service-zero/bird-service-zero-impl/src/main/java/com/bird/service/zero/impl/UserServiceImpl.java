package com.bird.service.zero.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bird.core.Check;
import com.bird.core.sso.LoginDTO;
import com.bird.core.sso.LoginResult;
import com.bird.core.sso.ticket.TicketInfo;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserRoleDTO;
import com.bird.service.zero.mapper.UserMapper;
import com.bird.service.zero.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/10/11.
 */
@Service
@CacheConfig(cacheNames = "zero_user")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.UserService")
public class UserServiceImpl extends AbstractService<UserMapper,User> implements UserService {

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return 用户
     */
    @Override
    public User getUserByLoginName(String userName) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("userName = {0} and delFlag = 0", userName);
        return selectOne(ew);
    }

    /**
     * 设置用户角色
     *
     * @param dto 用户角色信息
     */
    @Override
    public void setUserRoles(UserRoleDTO dto) {
        Check.NotNull(dto, "dto");
        Check.GreaterThan(dto.getUserId(), 0L, "dto.userId");

        mapper.deleteUserRoles(dto.getUserId());
        mapper.setUserRoles(dto);
    }

    /**
     * 获取用户拥有的所有角色Id
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        Check.GreaterThan(userId, 0L, "userId");
        return mapper.getUserRoleIds(userId);
    }

    /**
     * 获取用户拥有的权限集合
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<String> getUserPermissions(Long userId) {
        Check.GreaterThan(userId, 0L, "userId");
        return mapper.getUserPermissionNames(userId);
    }

    /**
     * 用户登录
     *
     * @return 登录结果
     */
    @Override
    public LoginResult Login(LoginDTO loginDTO) {
        Check.NotNull(loginDTO,"loginDTO");
        Check.NotEmpty(loginDTO.getUserName(),"userName");
        Check.NotEmpty(loginDTO.getPassword(),"password");

        User user = mapper.getUserByName(loginDTO.getUserName());
        if (user == null) {
            return LoginResult.Error("登录用户不存在.");
        }

        if(user.isLocked()){
            return LoginResult.Error("用户已被锁定.");
        }

        //TODO:密码加密验证
        String encryPassword = loginDTO.getPassword();
        if(!encryPassword.equals(user.getPassword())){
            return LoginResult.Error("密码错误.");
        }

        TicketInfo ticket = new TicketInfo();
        ticket.setUserId(user.getId().toString());
        ticket.setName(user.getUserName());

        return LoginResult.Success(ticket);
    }
}
