package com.bird.service.zero.dto;

import com.bird.core.service.AbstractDTO;
import com.bird.core.service.OptionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/11/20.
 */
public class DicDTO extends AbstractDTO {
    private Long id;
    private String placeholder;
    private String defaultCode;
    private List<OptionDTO> options;

    public DicDTO() {
        options = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }
}
