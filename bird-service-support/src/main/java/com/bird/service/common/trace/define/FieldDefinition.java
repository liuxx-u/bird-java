package com.bird.service.common.trace.define;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.bird.service.common.trace.TraceField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    public FieldDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public FieldDefinition(Field field) {
        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            this.name = tableField.value();
        } else {
            this.name = field.getName();
        }
        this.name = StringUtils.strip(this.name, StringPool.BACKTICK);

        if (field.isAnnotationPresent(TraceField.class)) {
            TraceField traceField = field.getAnnotation(TraceField.class);
            description = traceField.value();
        }
    }
}
