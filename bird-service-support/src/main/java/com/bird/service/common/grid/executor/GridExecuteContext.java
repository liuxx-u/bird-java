package com.bird.service.common.grid.executor;

import com.bird.service.common.grid.GridClassContainer;
import com.bird.service.common.grid.GridDefinition;
import com.bird.service.common.grid.enums.GridActionEnum;
import com.bird.service.common.grid.exception.GridException;
import com.bird.service.common.grid.interceptor.IGridInterceptor;
import com.bird.service.common.grid.interceptor.IGridQueryInterceptor;
import com.bird.service.common.grid.pojo.PagedListQuery;
import com.bird.service.common.grid.pojo.PagedResult;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @since 2021/1/22
 */
public class GridExecuteContext {

    private final BeanFactory beanFactory;
    private final GridClassContainer container;
    private final Map<DialectType, IGridExecutor> gridExecutors;
    private final Map<GridActionEnum, List<IGridInterceptor>> actionInterceptorMap;

    public GridExecuteContext(BeanFactory beanFactory, GridClassContainer container, IGridExecutorLoader gridExecutorLoader, List<IGridInterceptor> interceptors) {
        this.beanFactory = beanFactory;
        this.container = container;
        this.gridExecutors = gridExecutorLoader.loadExecutors();

        this.actionInterceptorMap = new HashMap<>(4);
        for (GridActionEnum actionEnum : GridActionEnum.values()) {
            List<IGridInterceptor> actionInterceptors = interceptors.stream()
                    .filter(i -> ArrayUtils.contains(i.actions(), actionEnum))
                    .sorted(Comparator.comparingInt(IGridInterceptor::getOrder))
                    .collect(Collectors.toList());
            actionInterceptorMap.put(actionEnum, actionInterceptors);
        }
    }

    /**
     * 执行表格操作
     *
     * @param gridName   表格名称
     * @param actionEnum 表格操作
     * @param inputData  传入的数据
     */
    public Object execute(String gridName, GridActionEnum actionEnum, Object inputData) {
        GridDefinition gridDefinition = this.container.getGridDefinition(gridName);
        List<IGridInterceptor> interceptors = this.actionInterceptorMap.get(actionEnum);

        int cursor = -1;
        for (IGridInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(actionEnum, gridDefinition, inputData)) {
                break;
            }
            cursor++;
        }

        Object outputData = this.doExecute(actionEnum, gridDefinition, inputData);

        while (cursor >= 0) {
            outputData = interceptors.get(cursor).afterHandle(actionEnum, gridDefinition, outputData);
            cursor--;
        }
        return outputData;
    }


    private Object doExecute(GridActionEnum actionEnum, GridDefinition gridDefinition, Object inputData) {
        IGridExecutor gridExecutor = this.gridExecutor(gridDefinition);


        switch (actionEnum) {
            case QUERY:
                IGridQueryInterceptor queryInterceptor = null;
                PagedListQuery query = (PagedListQuery) inputData;
                if (gridDefinition.getQueryInterceptorClass() != null) {
                    queryInterceptor = this.beanFactory.getBean(gridDefinition.getQueryInterceptorClass());
                }
                if (queryInterceptor != null) {
                    queryInterceptor.preQuery(query);
                }
                PagedResult<Map<String, Object>> result = gridExecutor.listPaged(gridDefinition, query);
                if (queryInterceptor != null) {
                    queryInterceptor.afterQuery(result);
                }
                return result;
            case INSERT:
                return gridExecutor.add(gridDefinition, (Map<String, Object>) inputData);
            case UPDATE:
                return gridExecutor.edit(gridDefinition, (Map<String, Object>) inputData);
            case DELETE:
                return gridExecutor.delete(gridDefinition, inputData);
            default:
                return null;
        }
    }

    /**
     * 获取表格查询执行器
     *
     * @param gridDefinition 表格描述符
     * @return 执行器
     */
    private IGridExecutor gridExecutor(GridDefinition gridDefinition) {
        if (gridDefinition == null) {
            throw new GridException("未找到对应的表格定义");
        }
        DialectType dialectType = gridDefinition.getDialectType();
        IGridExecutor gridExecutor = gridExecutors.get(dialectType);
        if (gridExecutor == null) {
            throw new GridException("不支持的表格处理器");
        }
        return gridExecutor;
    }
}
