package com.bird.dubbo.rpc.filter;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * dubbo提供者拦截器,用于从RpcContext中生成Session
 */
public class ProviderSessionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String json = RpcContext.getContext().getAttachment("dubbo-session");
        BirdSession session = JSON.parseObject(json,BirdSession.class);
        SessionContext.setSession(session);

        Result result = invoker.invoke(invocation);

        return result;
    }
}
