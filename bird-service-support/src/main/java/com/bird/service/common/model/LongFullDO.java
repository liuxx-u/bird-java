package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LongFullDO extends LongDO implements IHasCreatorId<String>,IHasModifierId<String> {

    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifierId;
}
