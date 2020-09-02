package com.bird.web.sso.client.permission;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/4/22
 */
public class DefaultUserPermissionChecker implements IUserPermissionChecker {

    @Override
    public boolean hasPermissions(String userId, List<String> permissions, boolean checkAll) {
        return true;
    }
}
