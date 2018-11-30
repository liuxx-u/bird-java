package com.bird.gateway.web.pipe;

import com.bird.gateway.common.enums.PipeTypeEnum;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public interface IChainPipe extends IPipe {

    /**
     * return plugin order .
     * This attribute To determine the plugin execution order in the same type plugin.
     *
     * @return int order
     */
    int getOrder();

    /**
     * return plugin type.
     * the plugin execution order
     * before type The first to perform then Function Type ,then last type.
     *
     * @return {@linkplain PipeTypeEnum}
     */
    PipeTypeEnum pipeType();

    /**
     * plugin is execute.
     * if return true this plugin can not execute.
     *
     * @param exchange the current server exchange
     * @return default false.
     */
    default Boolean skip(ServerWebExchange exchange) {
        return false;
    }
}
