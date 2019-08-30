package com.bird.service.sample.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Data
@TableName("tab_long")
public class TabLongDTO extends EntityDTO {
    private String remark;
}
