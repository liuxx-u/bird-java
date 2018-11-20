package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/11/2.
 */
@Getter
@Setter
@TableName("zero_dicItem")
public class DicItemDTO extends EntityDTO {
    private String name;
    private String code;
    private Long dicTypeId;
    private Integer orderNo;
    private Boolean disable;
}
