package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/10/30.
 */
@Getter
@Setter
@TableName("zero_menu")
public class Menu extends AbstractModel {
    private String name;
    private String url;
    private String icon;
    private String parentId;
    private Integer orderNo;
    private String permissionName;
    private Integer visitType;//'1.游客访问;2.登录访问;3.权限访问'
}
