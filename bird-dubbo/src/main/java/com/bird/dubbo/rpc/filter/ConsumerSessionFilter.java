package com.bird.dubbo.rpc.filter;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * dubbo消费者拦截器,用于向RpcContext中写入Session信息
 */
@Activate(group = Constants.CONSUMER)
public class ConsumerSessionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        BirdSession session = SessionContext.getSession();
        if (session != null) {
            RpcContext.getContext().setAttachment("dubbo-session", JSON.toJSONString(session));
        }

        return invoker.invoke(invocation);
    }
}
