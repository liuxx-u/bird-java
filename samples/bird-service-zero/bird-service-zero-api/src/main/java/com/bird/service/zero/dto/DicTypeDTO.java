package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/11/2.
 */
@Getter
@Setter
@TableName("zero_dicType")
public class DicTypeDTO extends EntityDTO {
    private String name;
    private String key;
    private Long parentId;

    @TableField(exist = false)
    private String parentKey;
    private String placeholder;
    private String remark;
    private String defaultCode;
}
