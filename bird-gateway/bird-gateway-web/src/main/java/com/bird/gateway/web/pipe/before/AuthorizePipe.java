package com.bird.gateway.web.pipe.before;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.gateway.common.enums.RpcTypeEnum;
import com.bird.gateway.common.RouteDefinition;
import com.bird.gateway.common.enums.PipeEnum;
import com.bird.gateway.common.enums.PipeTypeEnum;
import com.bird.gateway.common.exception.CommonErrorCode;
import com.bird.gateway.common.dto.JsonResult;
import com.bird.gateway.web.pipe.AbstractPipe;
import com.bird.gateway.web.pipe.IChainPipe;
import com.bird.gateway.web.pipe.PipeChain;
import com.bird.gateway.web.pipe.before.authorize.IAuthorizeManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@SuppressWarnings("all")
public class AuthorizePipe extends AbstractPipe implements IChainPipe {

    @Autowired
    private IAuthorizeManager authorizeManager;

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, PipeChain chain, RouteDefinition routeDefinition) {
        BirdSession session = authorizeManager.parseSession(exchange);

        if(StringUtils.equalsIgnoreCase(routeDefinition.getRpcType(), RpcTypeEnum.DUBBO.getName())){
            if(BooleanUtils.isNotTrue(routeDefinition.getAnonymous())){
                if(session == null){
                    return jsonResult(exchange,JsonResult.error(CommonErrorCode.UNAUTHORIZED,"需登录后才能访问"));
                }else {
                    String[] permissions = StringUtils.split(routeDefinition.getPermissions(),",");
                    if(!authorizeManager.checkPermissions(session,permissions,BooleanUtils.isTrue(routeDefinition.getCheckAll()))){
                        return jsonResult(exchange,JsonResult.error(CommonErrorCode.UNAUTHORIZED,"当前用户权限不足"));
                    }
                }
            }
        }

        SessionContext.setSession(session);
        return chain.execute(exchange);
    }

    @Override
    public int getOrder() {
        return PipeEnum.AUTH.getCode();
    }

    @Override
    public PipeTypeEnum pipeType() {
        return PipeTypeEnum.BEFORE;
    }

    @Override
    public String named() {
        return PipeEnum.AUTH.getName();
    }
}
