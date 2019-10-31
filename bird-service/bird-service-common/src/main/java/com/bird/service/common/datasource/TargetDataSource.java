package com.bird.service.common.datasource;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {

    /**
     * 获取要使用的数据源, 如果没值, 使用默认数据源
     * @return 要使用的数据源
     */
    String value();
}
