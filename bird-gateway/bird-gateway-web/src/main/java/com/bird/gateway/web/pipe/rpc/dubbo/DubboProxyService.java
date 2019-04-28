package com.bird.gateway.web.pipe.rpc.dubbo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bird.core.exception.UserFriendlyException;
import com.bird.gateway.common.constant.Constants;
import com.bird.gateway.common.dto.convert.DubboHandle;
import com.bird.gateway.common.exception.GatewayException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Slf4j
@SuppressWarnings("all")
public class DubboProxyService {

    private static final JSONObject EMPTY_BODY = new JSONObject();

    private static final Map<String, RegistryConfig> REGISTRY_CONFIG_MAP = Maps.newConcurrentMap();

    private static final Map<DubboHandle,ReferenceConfig<GenericService>> REFERENCE_CONFIG_MAP = Maps.newConcurrentMap();

    /**
     * dubbo rpc invoke.
     *
     * @param paramMap    request paramMap.
     * @param dubboHandle dubboHandle.
     * @return rpc result.
     * @throws GatewayException exception for rpc.
     */
    public Object genericInvoker(ServerWebExchange exchange, final DubboHandle dubboHandle) throws GatewayException {
        ReferenceConfig<GenericService> reference = buildReferenceConfig(dubboHandle);

        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();

        GenericService genericService = null;
        try {
            genericService = referenceConfigCache.get(reference);
        } catch (Exception ex) {
            REFERENCE_CONFIG_MAP.remove(dubboHandle);
            reference.destroy();
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
            if (StringUtils.equals(e.getExceptionClass(), UserFriendlyException.class.getName())) {
                throw new UserFriendlyException(e.getExceptionMessage());
            } else {
                throw new GatewayException(e.getMessage());
            }
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

        String base64 = exchange.getRequest().getHeaders().getFirst(Constants.DUBBO_PARAM_HEADER);
        if(StringUtils.isNotBlank(base64)){
            String body = null ;
            try {
                body = new String(Base64.getDecoder().decode(base64),"utf-8");
                param.put("body", GenericJsonDeserializer.parse(body));
            } catch (UnsupportedEncodingException e) {
                log.error("dubbo参数解析失败");
            }
        }
        return param;
    }

    private ReferenceConfig<GenericService> buildReferenceConfig(final DubboHandle dubboHandle) {
        ReferenceConfig<GenericService> reference = REFERENCE_CONFIG_MAP.get(dubboHandle);
        if (Objects.isNull(reference)) {
            reference = new ReferenceConfig<>();
            reference.setInterface(dubboHandle.getInterfaceName());
            reference.setGeneric(true);

            reference.setRegistry(cacheRegistry(dubboHandle));
            reference.setConsumer(getConsumer(dubboHandle));

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

            REFERENCE_CONFIG_MAP.put(dubboHandle, reference);
        }

        return reference;
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
