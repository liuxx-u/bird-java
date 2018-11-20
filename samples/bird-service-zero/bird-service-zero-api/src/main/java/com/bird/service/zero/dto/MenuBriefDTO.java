package com.bird.service.zero.dto;


import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/10/31.
 */
@Getter
@Setter
public class MenuBriefDTO extends EntityDTO {
    private String name;
    private String url;
    private String icon;
    private String parentId;
    private String permissionName;
}
