package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.mapper.permission.DataRule;
import com.bird.service.common.mapper.permission.NullDataRuleProvider;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_classify")
public class CmsClassify extends AbstractModel {
    @DataRule(name = "数据规则1")
    private String name;
    @DataRule(name = "数据规则2",provider = NullDataRuleProvider.class)
    private Long parentId;
    private String parentIds;
    private Integer orderNo;
    private String logo;
}
