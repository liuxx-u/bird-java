package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

/**
 * Created by liuxx on 2017/10/10.
 */

@Getter
@Setter
@TableName("zero_user")
public class User extends AbstractModel {
    private String userName;

    private String password;

    private String nickName;

    private String phoneNo;

    private Boolean locked;

    private Date lastLoginTime;
}
