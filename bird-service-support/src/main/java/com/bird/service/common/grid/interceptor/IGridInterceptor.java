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
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param inputData      传入的数据
     * @return 是否继续向后执行
     */
    default boolean preHandle(GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData) {
        return true;
    }

    /**
     * 后置拦截
     *
     * @param actionEnum     表格操作
     * @param gridDefinition 表格定义
     * @param outputData     传入的数据
     * @return 返回的数据
     */
    default Object afterHandle(GridActionEnum actionEnum, GridDefinition gridDefinition, Object outputData) {
        return outputData;
    }

    /**
     * 拦截的操作
     *
     * @return 默认拦截所有操作
     */
    default GridActionEnum[] actions() {
        return GridActionEnum.values();
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    default int getOrder(){
        return 0;
    }
}
