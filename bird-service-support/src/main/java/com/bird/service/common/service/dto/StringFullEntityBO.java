package com.bird.service.common.service.dto;

import com.bird.service.common.model.IHasCreatorId;
import com.bird.service.common.model.IHasModifierId;
import lombok.*;

/**
 * @author liuxx
 * @since 2020/6/22
 */
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class StringFullEntityBO extends StringEntityBO implements IHasCreatorId<String>, IHasModifierId<String> {

    private String creatorId;

    private String modifierId;
}
