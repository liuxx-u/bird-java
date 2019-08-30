package com.bird.service.sample.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.StringEntityDTO;
import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Data
@TableName("tab_string")
public class TabStringDTO extends StringEntityDTO {
    private String remark;
}
