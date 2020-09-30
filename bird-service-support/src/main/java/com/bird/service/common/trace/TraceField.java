package com.bird.service.common.trace;

import java.lang.annotation.*;

/**
 * @author shaojie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface TraceField {

    /**
     * 列描述
     * @return 描述信息
     */
    String value() default "";
}
