package com.bird.service.cms.dto;


import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmsContentDTO extends EntityDTO {
    private String title;
    private Long classifyId;
    private String brief;
    private String content;
    private String link;
    private String cover;
    private Integer orderNo;
    private Integer browserNo;
    private Integer praiseNo;
}
