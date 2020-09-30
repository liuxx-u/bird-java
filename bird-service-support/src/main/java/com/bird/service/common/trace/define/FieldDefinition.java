package com.bird.service.common.trace.define;

import com.bird.service.common.trace.TraceField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author shaojie
 */
@Setter@Getter
public class FieldDefinition implements Serializable {

    /**
     * 列名
     */
    private String name;
    /**
     * 描述信息
     */
    private String description;

    public FieldDefinition(Field field) {
        name = field.getName();
        if (field.isAnnotationPresent(TraceField.class)) {
            TraceField traceField = field.getAnnotation(TraceField.class);
            description = traceField.value();
        }
    }
}
