package com.bird.service.common.grid.permission;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import com.bird.service.common.grid.interceptor.IGridInterceptor;

/**
 * 表格权限拦截器
 *
 * @author liuxx
 * @since 2021/2/3
 */
public class GridPermissionInterceptor implements IGridInterceptor {
    /**
     * 前置拦截
     *
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param inputData      传入的数据
     */
    @Override
    public boolean preHandle(GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData) {
        return true;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
