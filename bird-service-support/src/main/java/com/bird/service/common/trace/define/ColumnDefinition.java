package com.bird.service.common.trace.define;

import com.bird.service.common.trace.ColumnTrace;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author shaojie
 */
@Setter@Getter
public class ColumnDefinition implements Serializable {

    /**
     * 列名
     */
    private String name;
    /**
     * 描述信息
     */
    private String description;

    public ColumnDefinition(Field field) {
        name = field.getName();
        if (field.isAnnotationPresent(ColumnTrace.class)) {
            ColumnTrace columnTrace = field.getAnnotation(ColumnTrace.class);
            description = columnTrace.value();
        }
    }
}
