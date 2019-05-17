package com.bird.dubbo.cluster.loadbalance;

import com.bird.core.utils.SystemHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/17
 */
public class IpFirstLoadBalance extends AbstractLoadBalance {

    private RandomLoadBalance defaultLoadBalance = new RandomLoadBalance();

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        List<String> ips = SystemHelper.getLocalIps();

        if (CollectionUtils.isNotEmpty(ips)) {
            for (Invoker<T> invoker : invokers) {
                if (ips.contains(invoker.getUrl().getHost())) {
                    return invoker;
                }
            }
        }

        return defaultLoadBalance.select(invokers,url,invocation);
    }
}
