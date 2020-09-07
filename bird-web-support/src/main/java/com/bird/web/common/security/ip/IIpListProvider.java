package com.bird.web.common.security.ip;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/9/4
 */
public interface IIpListProvider {

    /**
     * 获取ip白名单配置集合
     *
     * @return ip白名单配置集合
     */
    List<IpConfProperties> listIps();
}
