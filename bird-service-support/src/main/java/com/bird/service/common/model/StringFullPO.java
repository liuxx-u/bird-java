package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

/**
 * @author liuxx
 * @since 2020/6/19
 */
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public abstract class StringFullPO extends StringPO implements IHasCreatedBy, IHasUpdatedBy {

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
}
