package com.bird.web.common.security.ip;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @since 2020/9/4
 */
@Data
public class IpConfProperties {

    /**
     * 受控uri
     */
    private String uriPattern;
    /**
     * 合法ip，多个ip以逗号分隔
     */
    private String ips;

    public List<String> listIps() {
        if (StringUtils.isEmpty(this.ips)) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.ips.split(",")).collect(Collectors.toList());
    }
}
