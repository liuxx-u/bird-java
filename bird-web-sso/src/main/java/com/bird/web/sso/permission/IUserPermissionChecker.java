package com.bird.web.sso.permission;

public interface IUserPermissionChecker {

    /**
     * 检查用户是否拥有权限
     * @param userId 用户id
     * @param permissions 权限集合
     * @param checkAll 是否满足全部
     * @return
     */
    boolean hasPermissions(String userId,String[] permissions,boolean checkAll);

}
