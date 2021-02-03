package com.bird.service.common.grid.interceptor;

import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import org.springframework.core.Ordered;

/**
 * @author liuxx
 * @since 2021/2/3
 */
public interface IGridInterceptor extends Ordered {

    /**
     * 前置拦截
     *
     * @param girdName       表格名称
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param inputData      传入的数据
     */
    boolean preHandle(String girdName, GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData);

    /**
     * 拦截的操作
     *
     * @return 默认拦截所有操作
     */
    default GridActionEnum[] actions() {
        return GridActionEnum.values();
    }
}
