package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_site")
public class SiteDTO extends EntityDTO {
    private String name;
    private String key;
    @TableField("`zero_site`.`host`")
    private String host;
    private String indexUrl;
    private String loginNotifyUrl;
    private String remark;
    private Boolean disabled;
    private String permissionName;
}
