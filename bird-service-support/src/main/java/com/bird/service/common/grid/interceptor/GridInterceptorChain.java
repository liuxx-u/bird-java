package com.bird.service.common.grid.interceptor;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.Ordered;

import java.util.Comparator;
import java.util.List;

/**
 * @author liuxx
 * @since 2021/2/3
 */
public class GridInterceptorChain {

    private final List<IGridInterceptor> interceptors;

    public GridInterceptorChain(List<IGridInterceptor> interceptors) {
        this.interceptors = interceptors;
        this.interceptors.sort(Comparator.comparingInt(Ordered::getOrder));
    }

    /**
     * 前置拦截
     *
     * @param girdName       表格名称
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param inputData      传入的数据
     */
    public boolean preHandle(String girdName, GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData) {
        for (IGridInterceptor interceptor : this.interceptors) {
            if (!ArrayUtils.contains(interceptor.actions(), actionEnum)) {
                continue;
            }
            if (!interceptor.preHandle(girdName, actionEnum, gridDefinition, inputData)) {
                return false;
            }
        }
        return true;
    }
}
