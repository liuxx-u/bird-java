package com.bird.service.common.grid.scanner;

import com.bird.core.utils.ClassHelper;
import com.bird.service.common.grid.annotation.AutoGrid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liuxx
 * @since 2021/1/18
 */
@Slf4j
public class SpringGridDefinitionScanner implements IGridDefinitionScanner {

    private static final int DEFAULT_CAPACITY = 1 << 8;

    /**
     * 通过包名扫描表格定义{@link AutoGrid}信息
     *
     * @param packagePatterns 包名
     * @return 表格定义数据
     */
    @Override
    public Map<String, Class<?>> scan(String... packagePatterns) {
        Set<Class<?>> classes = this.scanClasses(packagePatterns);

        Map<String, Class<?>> map = new HashMap<>(DEFAULT_CAPACITY);
        for (Class<?> clazz : classes) {
            AutoGrid autoGrid = clazz.getAnnotation(AutoGrid.class);
            if (autoGrid == null) {
                continue;
            }

            String gridName = autoGrid.name();
            if (StringUtils.isBlank(gridName)) {
                gridName = clazz.getName();
            }
            map.put(gridName, clazz);
        }

        return map;
    }

    /**
     * 获取包名下标记了{@link AutoGrid}的类
     *
     * @param packagePatterns 包名;
     * @return 类集合
     */
    private Set<Class<?>> scanClasses(String... packagePatterns) {
        Set<Class<?>> classes = new HashSet<>();

        for (String packagePattern : packagePatterns) {
            try {
                classes.addAll(ClassHelper.scanAnnotationClasses(packagePattern, AutoGrid.class));
            } catch (IOException e) {
                log.error("{}包扫码失败", packagePattern, e);
            }
        }

        return classes;
    }
}
