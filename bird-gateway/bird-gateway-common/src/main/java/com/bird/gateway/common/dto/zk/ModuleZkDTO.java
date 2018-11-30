package com.bird.gateway.common.dto.zk;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Data
public class ModuleZkDTO implements Serializable {
    /**
     * 模块路径
     */
    private String path;
    /**
     * 模块名称
     */
    private String name;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 子模块集合
     */
    private List<ModuleZkDTO> children;
    /**
     * 应用的插件集合
     */
    private List<PluginZkDTO> plugins;
}
