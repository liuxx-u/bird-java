package com.bird.gateway.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/11/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginZkDTO implements Serializable {
    private String id;

    private String name;

    private Boolean enabled;
}
