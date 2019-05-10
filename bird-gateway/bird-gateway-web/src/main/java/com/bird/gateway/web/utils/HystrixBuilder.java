package com.bird.gateway.web.utils;

import com.bird.gateway.common.GatewayConstant;
import com.bird.gateway.common.dto.convert.HystrixHandle;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

/**
 * @author liuxx
 * @date 2018/11/29
 */
public class HystrixBuilder {

    /**
     * this is build HystrixObservableCommand.Setter.
     *
     * @param hystrixHandle {@linkplain HystrixHandle}
     * @return {@linkplain HystrixObservableCommand.Setter}
     */
    public static HystrixObservableCommand.Setter build(final HystrixHandle hystrixHandle) {

        if (hystrixHandle.getMaxConcurrentRequests() == 0) {
            hystrixHandle.setMaxConcurrentRequests(GatewayConstant.MAX_CONCURRENT_REQUESTS);
        }
        if (hystrixHandle.getErrorThresholdPercentage() == 0) {
            hystrixHandle.setErrorThresholdPercentage(GatewayConstant.ERROR_THRESHOLD_PERCENTAGE);
        }
        if (hystrixHandle.getRequestVolumeThreshold() == 0) {
            hystrixHandle.setRequestVolumeThreshold(GatewayConstant.REQUEST_VOLUME_THRESHOLD);
        }
        if (hystrixHandle.getSleepWindowInMilliseconds() == 0) {
            hystrixHandle.setSleepWindowInMilliseconds(GatewayConstant.SLEEP_WINDOW_INMILLISECONDS);
        }

        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey(hystrixHandle.getGroupKey());

        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey(hystrixHandle.getCommandKey());

        final HystrixCommandProperties.Setter propertiesSetter =
                HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(hystrixHandle.getTimeout())
                        .withCircuitBreakerEnabled(true)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(hystrixHandle.getMaxConcurrentRequests())
                        .withCircuitBreakerErrorThresholdPercentage(hystrixHandle.getErrorThresholdPercentage())
                        .withCircuitBreakerRequestVolumeThreshold(hystrixHandle.getRequestVolumeThreshold())
                        .withCircuitBreakerSleepWindowInMilliseconds(hystrixHandle.getSleepWindowInMilliseconds());

        return HystrixObservableCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andCommandPropertiesDefaults(propertiesSetter);
    }

}
