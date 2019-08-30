package com.bird.service.sample.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.LongModel;
import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Data
@TableName("tab_long")
public class TabLong extends LongModel {
    private String remark;
}
