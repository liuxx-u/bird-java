package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.service.AbstractDTO;

/**
 * Created by liuxx on 2017/10/10.
 */
@TableName("zero_user")
public class UserDTO extends AbstractDTO {
    private String userName;
    private String nickName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
