package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.model.AbstractModel;

import java.sql.Date;

/**
 * Created by liuxx on 2017/10/10.
 */

@TableName("zero_user")
public class User extends AbstractModel {
    private String userName;

    private String password;

    private String nickName;

    private Date lastLoginTime;
}
