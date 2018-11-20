package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by liuxx on 2017/10/10.
 */
@Getter
@Setter
@TableName("zero_user")
public class UserDTO extends EntityDTO {
    private String userName;

    private String nickName;

    private String phoneNo;

    private Boolean locked;

    private Date lastLoginTime;
}
