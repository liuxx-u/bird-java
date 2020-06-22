package com.bird.service.common.service.dto;

import com.bird.service.common.model.IHasCreatorId;
import com.bird.service.common.model.IHasModifierId;

/**
 * @author liuxx
 * @since 2020/6/22
 */
public class StringFullEntityBO extends StringEntityBO implements IHasCreatorId<String>, IHasModifierId<String> {

    private String creatorId;

    private String modifierId;

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getModifierId() {
        return modifierId;
    }

    @Override
    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }
}
