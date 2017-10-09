package com.bird.common.dto;

import com.bird.core.service.SerializableDTO;

/**
 * Created by liuxx on 2017/7/4.
 */
public class AlbumDTO extends SerializableDTO {
    private String path;
    private String description;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
