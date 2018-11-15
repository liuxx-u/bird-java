package com.bird.core.initialize;

import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.SpringContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 初始化执行器
 * @author liuxx
 * @date 2018/11/15
 */
public class InitializeExecutor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 扫描的包名
     */
    private String basePackage;
    /**
     * 是否已经初始化
     */
    private boolean initialized;

    /**
     * 构造函数
     */
    public InitializeExecutor(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 初始化
     */
    public void initialize() {
        if(this.initialized){
            logger.warn("current project has been initialized.");
            return;
        }
        if (StringUtils.isBlank(this.basePackage)) {
            logger.warn("initialize basePackage is empty");
            return;
        }
        List<IInitializePipe> pipes = new ArrayList<>(SpringContextHolder.getBeansOfType(IInitializePipe.class).values());
        if (CollectionUtils.isEmpty(pipes)) {
            logger.warn("initialize pipes is empty");
            return;
        }
        Set<Class<?>> classes = ClassHelper.getClasses(this.basePackage);
        if (CollectionUtils.isEmpty(classes)) {
            logger.warn("there are no class in basePackage:" + this.basePackage);
            return;
        }

        Collections.sort(pipes);
        for (IInitializePipe pipe : pipes) {
            String pipeName = pipe.getClass().getName();
            logger.info("start initialize pipe :" + pipeName);
            for (Class<?> clazz : classes) {
                pipe.scanClass(clazz);
            }
            pipe.initialize();
            logger.info(pipe.getClass().getName() + " initialized.");
        }
        this.initialized = true;
    }
}
