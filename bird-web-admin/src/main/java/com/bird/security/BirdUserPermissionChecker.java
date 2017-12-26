package com.bird.security;

import com.bird.core.sso.permission.UserPermissionChecker;
import com.bird.service.zero.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;

public class BirdUserPermissionChecker implements UserPermissionChecker {

    @Autowired
    private UserService userService;

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
        Long uid = Long.parseLong(userId);
        List<String> userPermissions = userService.getUserPermissions(uid);

        List<String> checkPermissions = Arrays.asList(permissions);

        if (checkAll) return userPermissions.containsAll(checkPermissions);

        for (String permission : permissions) {
            if (userPermissions.contains(permission)) return true;
        }

        //TODO：非测试环境，改为false
        return true;
    }
}
