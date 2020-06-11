package com.bird.web.common.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 版本号
 *
 * @author liuxx
 * @since 2020/6/11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    String value();
}
