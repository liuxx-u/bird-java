package com.bird.service.cms.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_classify")
public class CmsClassifyDTO extends EntityDTO {
    @TableField("`cms_classify`.`name`")
    private String name;
    private Long parentId;
    @TableField("`cms_classify`.`parentIds`")
    private String parentIds;
    private Integer orderNo;
    private String logo;
}
