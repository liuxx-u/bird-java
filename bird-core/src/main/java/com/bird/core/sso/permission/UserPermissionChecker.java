package com.bird.core.sso.permission;

public interface UserPermissionChecker {

    /**
     * 检查用户是否拥有权限
     * @param userId 用户id
     * @param permissions 权限集合
     * @param checkAll 是否满足全部
     * @return
     */
    boolean hasPermissions(String userId,String[] permissions,boolean checkAll);

}
