package com.bird.gateway.web.pipe.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.exception.GatewayException;
import com.bird.gateway.common.utils.ByteBuffUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Slf4j
@SuppressWarnings("all")
public class DubboProxyService {

    private static final JSONObject EMPTY_BODY = new JSONObject();

    private static final Map<String, RegistryConfig> REGISTRY_CONFIG_MAP = Maps.newConcurrentMap();

    private static final Map<String, ApplicationConfig> APPLICATION_CONFIG_MAP = Maps.newConcurrentMap();

    /**
     * dubbo rpc invoke.
     *
     * @param paramMap    request paramMap.
     * @param dubboHandle dubboHandle.
     * @return rpc result.
     * @throws SoulException exception for rpc.
     */
    public Object genericInvoker(ServerWebExchange exchange, final DubboHandle dubboHandle) throws GatewayException {
        ReferenceConfig<GenericService> reference = buildReferenceConfig(dubboHandle);

        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();

        GenericService genericService = null;
        try {
            genericService = referenceConfigCache.get(reference);
        } catch (Exception ex) {
            referenceConfigCache.destroy(reference);
            log.error(dubboHandle.getInterfaceName() + "服务连接失败");
            throw new GatewayException(ex.getMessage());
        }

        final Map<String, Object> paramMap = resolveParam(exchange);
        final Pair<String[], Object[]> pair = buildParameter(paramMap, dubboHandle);

        try {
            return genericService.$invoke(dubboHandle.getMethodName(), pair.getLeft(), pair.getRight());
        } catch (GenericException e) {
            log.error(e.getExceptionMessage());
            throw new GatewayException(e.getMessage());
        }
    }

    private Pair<String[], Object[]> buildParameter(final Map<String, Object> paramMap, DubboHandle dubboHandle) {

        List<String> paramList = Lists.newArrayList();
        List<Object> args = Lists.newArrayList();

        Map<String, String> paramTypeMap = JSON.parseObject(dubboHandle.getParameters(), LinkedHashMap.class);
        Object body = paramMap.getOrDefault("body", EMPTY_BODY);
        for (String key : paramTypeMap.keySet()) {
            String paramType = paramTypeMap.get(key);
            Object paramValue = paramMap.getOrDefault(key, body);

            paramList.add(paramType);
            args.add(paramValue);
        }
        return new ImmutablePair<>(paramList.toArray(new String[0]), args.toArray());
    }

    private Map<String, Object> resolveParam(ServerWebExchange exchange) {
        JSONObject param = new JSONObject();

        MultiValueMap<String, String> pathParams = exchange.getRequest().getQueryParams();
        for (String key : pathParams.keySet()) {
            param.put(key, pathParams.getFirst(key));
        }

        AtomicReference<String> json = new AtomicReference<>("");
        DataBufferUtils.join(exchange.getRequest().getBody())
                .subscribe(dataBuffer -> json.set(ByteBuffUtils.byteBufferToString(dataBuffer.asByteBuffer())));

        param.put("body", GenericJsonDeserializer.parse(json.get()));
        return param;
    }

    private ReferenceConfig<GenericService> buildReferenceConfig(final DubboHandle dubboHandle) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setGeneric(true);

        final ApplicationConfig applicationConfig = cacheApplication(dubboHandle);
        reference.setApplication(applicationConfig);

        reference.setRegistry(cacheRegistry(dubboHandle));
        reference.setConsumer(getConsumer(dubboHandle));

        reference.setInterface(dubboHandle.getInterfaceName());
        if (StringUtils.isNoneBlank(dubboHandle.getVersion())) {
            reference.setVersion(dubboHandle.getVersion());
        }
        if (StringUtils.isNoneBlank(dubboHandle.getProtocol())) {
            reference.setProtocol(dubboHandle.getProtocol());
        }
        if (StringUtils.isNoneBlank(dubboHandle.getGroup())) {
            reference.setGroup(dubboHandle.getGroup());
        }
        if (StringUtils.isNoneBlank(dubboHandle.getLoadBalance())) {
            reference.setLoadbalance(dubboHandle.getLoadBalance());
        }

        Optional.ofNullable(dubboHandle.getTimeout()).ifPresent(reference::setTimeout);
        Optional.ofNullable(dubboHandle.getRetries()).ifPresent(reference::setRetries);

        return reference;
    }

    private ApplicationConfig cacheApplication(final DubboHandle dubboHandle) {
        String appName = dubboHandle.getAppName();
        ApplicationConfig applicationConfig = APPLICATION_CONFIG_MAP.get(appName);
        if (Objects.isNull(applicationConfig)) {
            applicationConfig = new ApplicationConfig(appName);
            APPLICATION_CONFIG_MAP.put(appName, applicationConfig);
        }
        return applicationConfig;
    }

    private RegistryConfig cacheRegistry(final DubboHandle dubboHandle) {
        String appName = dubboHandle.getAppName();
        String registry = dubboHandle.getRegistry();
        RegistryConfig registryConfig = REGISTRY_CONFIG_MAP.get(appName);
        if (Objects.isNull(registryConfig)) {
            registryConfig = new RegistryConfig(registry);
            REGISTRY_CONFIG_MAP.put(appName, registryConfig);
        }
        return registryConfig;
    }

    private ConsumerConfig getConsumer(final DubboHandle dubboHandle){
        String appName = dubboHandle.getAppName();
        Integer timeout = dubboHandle.getTimeout();

        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(timeout);
        consumerConfig.setRetries(0);
        consumerConfig.setLoadbalance("leastactive");
        consumerConfig.setCluster("failfast");
        consumerConfig.setFilter("consumerSession");
        return consumerConfig;
    }
}
