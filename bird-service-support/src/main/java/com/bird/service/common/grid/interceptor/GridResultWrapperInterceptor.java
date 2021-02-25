package com.bird.service.common.grid.interceptor;

import com.bird.core.Result;
import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import org.springframework.core.Ordered;

/**
 * @author liuxx
 * @since 2021/2/7
 */
public class GridResultWrapperInterceptor implements IGridInterceptor {

    @Override
    public Object afterHandle(GridActionEnum actionEnum, GridDefinition gridDefinition, Object outputData) {
        return Result.success("success", outputData);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
