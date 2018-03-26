package com.bird.web.api.configure;

import com.bird.web.sso.permission.IUserPermissionChecker;
import org.springframework.stereotype.Component;

/**
 * @author liuxx
 * @date 2018/3/26
 */
@Component
public class CustomerUserPermissionChecker implements IUserPermissionChecker {
    /**
     * 检查用户是否拥有权限
     *
     * @param userId      用户id
     * @param permissions 权限集合
     * @param checkAll    是否满足全部
     * @return
     */
    @Override
    public boolean hasPermissions(String userId, String[] permissions, boolean checkAll) {
        return true;
    }
}
