package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;

/**
 * @author liuxx
 * @date 2019/7/11
 */
@Deprecated
public abstract class AbstractFullModel extends AbstractModel implements IHasCreatorId<Long>,IHasModifierId<Long> {

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(fill = FieldFill.UPDATE)
    private Long modifierId;

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }
}
