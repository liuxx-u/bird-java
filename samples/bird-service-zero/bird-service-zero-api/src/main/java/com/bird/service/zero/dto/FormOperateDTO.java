package com.bird.service.zero.dto;

import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FormOperateDTO extends EntityDTO {
    private Boolean withTab;
    private String saveUrl;
    private String tabType;
    private String tabPosition;
    private String defaultGroupName;
    private Integer lineCapacity;
    private List<FieldDTO> fields;

    public FormOperateDTO(){
        fields = new ArrayList<>();
    }
}
