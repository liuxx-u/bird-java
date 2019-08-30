package com.bird.service.sample.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.StringModel;
import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Data
@TableName("tab_string")
public class TabString extends StringModel {

    private String remark;
}
