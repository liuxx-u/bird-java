package com.bird.service.zero.dto;

import com.bird.service.common.service.dto.AbstractDTO;
import com.bird.service.common.service.dto.OptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/11/20.
 */
@Getter
@Setter
public class DicDTO extends AbstractDTO {
    private Long id;
    private String placeholder;
    private String defaultCode;
    private List<OptionDTO> options;

    public DicDTO() {
        options = new ArrayList<>();
    }
}
