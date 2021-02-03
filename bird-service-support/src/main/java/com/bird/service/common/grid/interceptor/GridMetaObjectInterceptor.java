package com.bird.service.common.grid.interceptor;

import com.bird.core.session.SessionContext;
import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Slf4j
public class GridMetaObjectInterceptor implements IGridInterceptor {
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
        if (!(inputData instanceof Map)) {
            log.warn("传入的参数不是Map类型");
            return false;
        }

        Date current = new Date();

        Map<String, Object> pojo = (Map<String, Object>) inputData;
        pojo.put("modifierId", SessionContext.getUserId());
        pojo.put("modifiedTime", current);
        if (actionEnum == GridActionEnum.INSERT) {
            pojo.put("delFlag", 0);
            pojo.put("createTime", current);
            pojo.put("creatorId", SessionContext.getUserId());
        }
        return true;
    }

    /**
     * 拦截的操作
     *
     * @return 拦截新增和编辑操作
     */
    @Override
    public GridActionEnum[] actions() {
        return new GridActionEnum[]{GridActionEnum.INSERT, GridActionEnum.UPDATE};
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
        return 0;
    }
}
