package com.bird.gateway.common;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public interface GatewayConstant {
    /**
     * The constant REQUESTDTO.
     */
    String REQUESTDTO = "requestDTO";

    /**
     * The constant ROUTE_DEFINITION.
     */
    String ROUTE_DEFINITION="route_definition";

    /**
     * The constant RPC_TYPE.
     */
    String RPC_TYPE = "rpc";

    /**
     * The constant DUBBO_PARAM_HEADER.
     */
    String DUBBO_PARAM_HEADER = "dubbo-param";

    /**
     * The constant DUBBO_RPC_RESULT.
     */
    String DUBBO_RPC_RESULT = "dubbo_rpc_result";

    /**
     * The constant DUBBO_ERROR_MESSAGE.
     */
    String DUBBO_ERROR_MESSAGE = "dubbo_error_message";

    /**
     * The constant HTTP_RPC_RESULT.
     */
    String HTTP_RPC_RESULT = "http_rpc_result";

    /**
     * The constant CLIENT_RESPONSE_RESULT_TYPE.
     */
    String RESPONSE_RESULT_TYPE = "response_result_type";

    /**
     * The constant APP_KEY.
     */
    String APP_KEY = "appKey";

    /**
     * The constant SIGN.
     */
    String SIGN = "sign";

    /**
     * The constant TIMESTAMP.
     */
    String TIMESTAMP = "timestamp";

    /**
     * The constant ERROR_RESULT.
     */
    String ERROR_RESULT = "系统走神了,请稍候再试.";

    /**
     * hystrix withExecutionIsolationSemaphoreMaxConcurrentRequests.
     */
    int MAX_CONCURRENT_REQUESTS = 100;

    /**
     * hystrix  withCircuitBreakerErrorThresholdPercentage.
     */
    int ERROR_THRESHOLD_PERCENTAGE = 50;

    /**
     * hystrix withCircuitBreakerRequestVolumeThreshold.
     */
    int REQUEST_VOLUME_THRESHOLD = 20;

    /**
     * hystrix withCircuitBreakerSleepWindowInMilliseconds.
     */
    int SLEEP_WINDOW_INMILLISECONDS = 5000;

    /**
     * The constant TIME_OUT.
     */
    int TIME_OUT = 3000;
}
