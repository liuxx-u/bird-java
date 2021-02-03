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
     * @param girdName       表格名称
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param inputData      传入的数据
     */
    @Override
    public boolean preHandle(String girdName, GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData) {
        return true;
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
