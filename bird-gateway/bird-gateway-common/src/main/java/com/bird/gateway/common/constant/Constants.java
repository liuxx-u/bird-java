package com.bird.gateway.common.constant;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public interface Constants {
    /**
     * The constant REQUESTDTO.
     */
    String REQUESTDTO = "requestDTO";

    /**
     * The constant ROUTE_DEFINITION.
     */
    String ROUTE_DEFINITION="route_definition";

    /**
     * The constant CLIENT_RESPONSE_ATTR.
     */
    String CLIENT_RESPONSE_ATTR = "webHandlerClientResponse";

    /**
     * The constant DUBBO_RPC_RESULT.
     */
    String DUBBO_RPC_RESULT = "dubbo_rpc_result";

    /**
     * The constant CLIENT_RESPONSE_RESULT_TYPE.
     */
    String CLIENT_RESPONSE_RESULT_TYPE = "webHandlerClientResponseResultType";

    /**
     * The constant MODULE.
     */
    String MODULE = "module";

    /**
     * The constant METHOD.
     */
    String METHOD = "method";

    /**
     * The constant CONTENT.
     */
    String CONTENT = "content";

    /**
     * The constant APP_KEY.
     */
    String APP_KEY = "appKey";

    /**
     * The constant EXT_INFO.
     */
    String EXT_INFO = "extInfo";

    /**
     * The constant HTTP_METHOD.
     */
    String HTTP_METHOD = "httpMethod";

    /**
     * The constant RPC_TYPE.
     */
    String RPC_TYPE = "rpcType";

    /**
     * The constant SIGN.
     */
    String SIGN = "sign";

    /**
     * The constant TIMESTAMP.
     */
    String TIMESTAMP = "timestamp";

    /**
     * The constant RETRY.
     */
    int RETRY = 3;

    /**
     * The constant SOUL_DISRUPTOR_THREAD_NAME.
     */
    String SOUL_DISRUPTOR_THREAD_NAME = "soul-disruptor";

    /**
     * The constant SOUL_THREAD_NAME.
     */
    String SOUL_THREAD_NAME = "soul-thread";

    /**
     * The constant REJECT_MSG.
     */
    String REJECT_MSG = " You are forbidden to visit";

    /**
     * The constant REWRITE_URI.
     */
    String REWRITE_URI = "rewrite_uri";

    /**
     * The constant ERROR_RESULT.
     */
    String ERROR_RESULT = "系统走神了,请稍候再试.";

    /**
     * The constant TIMEOUT_RESULT.
     */
    String TIMEOUT_RESULT = "请求超时，请稍后重试.";

    /**
     * The constant UPSTREAM_NOT_FIND.
     */
    String UPSTREAM_NOT_FIND = "this can not rule upstream please check you config!";

    /**
     * The constant TOO_MANY_REQUESTS.
     */
    String TOO_MANY_REQUESTS = "the request is too fast please try again later";

    /**
     * The constant SIGN_IS_NOT_PASS.
     */
    String SIGN_IS_NOT_PASS = "sign is not pass,Please check you sign algorithm!";

    /**
     * The constant LINE_SEPARATOR.
     */
    String LINE_SEPARATOR = System.getProperty("line.separator");

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

    /**
     * The constant COLONS.
     */
    String COLONS = ":";
}
