package com.bird.web.common.security.ip;

import java.util.List;

/**
 * 默认的ip白名单配置提供者，从配置文件中读取ip白名单配置
 *
 * @author liuxx
 * @since 2020/9/4
 */
public class DefaultIpListProvider implements IIpListProvider {

    private final List<IpConfProperties> ipConfProperties;

    public DefaultIpListProvider(List<IpConfProperties> ipConfProperties) {
        this.ipConfProperties = ipConfProperties;
    }


    /**
     * 获取ip白名单配置集合
     *
     * @return ip白名单配置集合
     */
    @Override
    public List<IpConfProperties> listIps() {
        return this.ipConfProperties;
    }
}
