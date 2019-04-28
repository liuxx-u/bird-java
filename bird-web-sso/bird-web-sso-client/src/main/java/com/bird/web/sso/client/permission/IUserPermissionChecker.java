package com.bird.web.sso.client.permission;

/**
 * @author liuxx
 * @date 2019/3/6
 */
public interface IUserPermissionChecker {

    /**
     * 检查用户是否拥有权限
     *
     * @param userId      用户id
     * @param permissions 权限集合
     * @param checkAll    是否满足全部
     * @return true or false
     */
    boolean hasPermissions(String userId, String[] permissions, boolean checkAll);
}
