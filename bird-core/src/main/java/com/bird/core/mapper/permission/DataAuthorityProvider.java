package com.bird.core.mapper.permission;

import java.util.Set;

/**
 * @author liuxx
 * @date 2018/1/25
 *
 * 数据权限提供者，由使用者提供并注入spring容器
 */
public interface DataAuthorityProvider {

    /**
     * 获取下属用户id集合
     * 此方法所涉及到的mabatis数据库访问，一定不要再使用数据权限过滤，否则将造成死循环。
     *
     * @param userId 当前用户id
     * @return
     */
    Set<Long> getSubordinateUserIds(Long userId);
}
