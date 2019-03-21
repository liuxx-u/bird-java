package com.bird.core.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2018/4/17
 *
 * OkHttp工具类
 */
public final class OkHttpHelper {
    /**
     * JSON MediaType
     */
    public final static MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");

    private static Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    /**
     * OkHttpClient实例
     */
    private final static OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private OkHttpHelper() {
    }


    public static OkHttpClient client() {
        return CLIENT;
    }

    /**
     * 同步请求
     *
     * @param request 请求体
     * @return 结果字符串
     */
    public static String request(Request request) {
        try {
            Response response = CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            logger.error("请求失败", ex);
            return null;
        }
    }

    /**
     * 同步请求
     *
     * @param request 请求体
     * @param clazz   返回的结果类型
     * @param <T>     目标类型
     * @return 目标类型的实例
     */
    public static <T> T request(Request request, Class<T> clazz) {
        String result = request(request);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步请求
     *
     * @param request  请求体
     * @param callback 回调方法
     */
    public static void requestAsync(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 同步 简单get请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @return 结果字符串
     */
    public static String get(String url, Map<String, String> headers) {

        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }

        Request request = builder.build();
        return request(request);
    }

    /**
     * 同步 简单的get请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @param clazz   返回的结果类型
     * @param <T>     目标类型
     * @return 目标类型的实例
     */
    public static <T> T get(String url, Map<String, String> headers, Class<T> clazz) {
        String result = get(url, headers);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步 简单的get请求
     *
     * @param url      URL地址
     * @param headers  请求头
     * @param callback 回调方法
     */
    public static void getAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }

        Request request = builder.build();
        requestAsync(request, callback);
    }

    /**
     * 同步 post请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @param body    请求体
     * @return 结果字符串
     */
    public static String postJson(String url, Map<String, String> headers, Object body) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        return post(url, headers, requestBody);
    }

    /**
     * 同步 post请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @param body    请求体
     * @param clazz   返回的结果类型
     * @param <T>     目标类型
     * @return 目标类型的实例
     */
    public static <T> T postJson(String url, Map<String, String> headers, Object body, Class<T> clazz) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        return post(url, headers, requestBody, clazz);
    }

    /**
     * 异步 post请求
     *
     * @param url      URL地址
     * @param headers  请求头
     * @param body     请求体
     * @param callback 回调方法
     */
    public static void postJsonAsync(String url, Map<String, String> headers, Object body, Callback callback) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        postAsync(url, headers, requestBody, callback);
    }

    /**
     * 同步 post请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @param body    请求体
     * @return 结果字符串
     */
    public static String post(String url, Map<String, String> headers, RequestBody body) {
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }
        Request request = builder.post(body).build();
        return request(request);
    }

    /**
     * 同步 post请求
     *
     * @param url     URL地址
     * @param headers 请求头
     * @param body    请求体
     * @param clazz   返回的结果类型
     * @param <T>     目标类型
     * @return 目标类型的实例
     */
    public static <T> T post(String url, Map<String, String> headers, RequestBody body, Class<T> clazz) {
        String result = post(url, headers, body);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步 post请求
     *
     * @param url      URL地址
     * @param headers  请求头
     * @param body     请求体
     * @param callback 回调方法
     */
    public static void postAsync(String url, Map<String, String> headers, RequestBody body, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }
        Request request = builder.post(body).build();
        requestAsync(request, callback);
    }
}
