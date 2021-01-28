package com.bird.service.common.grid;

import com.bird.service.common.grid.annotation.AutoGrid;
import com.bird.service.common.grid.scanner.IGridDefinitionScanner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2021/1/18
 */
public class GridClassContainer implements InitializingBean {

    private final static ConcurrentHashMap<String, GridDefinition> GRID_DESCRIPTOR_MAP = new ConcurrentHashMap<>();

    private final AutoGridProperties gridProperties;
    private final IGridDefinitionScanner scanner;

    public GridClassContainer(AutoGridProperties gridProperties, IGridDefinitionScanner scanner){
        this.gridProperties = gridProperties;
        this.scanner = scanner;
    }

    /**
     * 根据表格名获取定义的{@link AutoGrid}类
     * @param gridName 表格名称
     * @return {@link AutoGrid}类
     */
    public GridDefinition getGridDefinition(String gridName) {
        if (StringUtils.isBlank(gridName)) {
            return null;
        }
        return GRID_DESCRIPTOR_MAP.get(gridName);
    }



    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     */
    @Override
    public void afterPropertiesSet() {
        Map<String, GridDefinition> definitions = this.scanner.scan(this.gridProperties.getBasePackages());
        GRID_DESCRIPTOR_MAP.putAll(definitions);
    }
}
