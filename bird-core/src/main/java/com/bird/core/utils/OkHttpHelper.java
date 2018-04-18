package com.bird.core.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    public static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");

    private static Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    /**
     * OkHttpClient实例
     */
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

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
        }catch (IOException ex){
            logger.error("请求失败",ex);
            return null;
        }
    }

    /**
     * 同步请求
     *
     * @param request 请求体
     * @param clazz   返回的结果类型
     * @param <T>
     * @return 目标类型的实例
     */
    public static <T> T request(Request request, Class<T> clazz) {
        String result = request(request);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步请求
     *
     * @param request 请求体
     * @param callback 回调方法
     */
    public static void requestAsync(Request request,Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 同步 简单get请求
     *
     * @param url URL地址
     * @return 结果字符串
     */
    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        return request(request);
    }

    /**
     * 同步 简单的get请求
     *
     * @param url   URL地址
     * @param clazz 返回的结果类型
     * @param <T>
     * @return 目标类型的实例
     */
    public static <T> T get(String url, Class<T> clazz) {
        String result = get(url);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步 简单的get请求
     * @param url URL地址
     * @param callback 回调方法
     */
    public static void getAsync(String url,Callback callback){
        Request request = new Request.Builder().url(url).build();
        requestAsync(request,callback);
    }

    /**
     * 同步 post请求
     *
     * @param url  URL地址
     * @param body 请求体
     * @return 结果字符串
     */
    public static String postJson(String url, Object body) {
        return post(url, body, MEDIA_JSON);
    }

    /**
     * 同步 post请求
     *
     * @param url   URL地址
     * @param body  请求体
     * @param clazz 返回的结果类型
     * @param <T>
     * @return 目标类型的实例
     */
    public static <T> T postJson(String url, Object body, Class<T> clazz) {
        return post(url, body, MEDIA_JSON, clazz);
    }

    /**
     * 异步 post请求
     * @param url URL地址
     * @param body 请求体
     * @param callback 回调方法
     */
    public static void postJsonAsync(String url,Object body,Callback callback){
        postAsync(url,body,MEDIA_JSON,callback);
    }

    /**
     * 同步 post请求
     *
     * @param url       URL地址
     * @param body      请求体
     * @param mediaType 媒体类型
     * @return 结果字符串
     */
    public static String post(String url, Object body, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(body));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return request(request);
    }

    /**
     * 同步 post请求
     *
     * @param url       URL地址
     * @param body      请求体
     * @param mediaType 媒体类型
     * @param clazz     返回的结果类型
     * @param <T>
     * @return 目标类型的实例
     */
    public static <T> T post(String url, Object body, MediaType mediaType, Class<T> clazz) {
        String result = post(url, body, mediaType);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 异步 post请求
     * @param url URL地址
     * @param body 请求体
     * @param mediaType 媒体类型
     * @param callback 回调方法
     */
    public static void postAsync(String url, Object body, MediaType mediaType,Callback callback){
        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(body));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        requestAsync(request,callback);
    }
}
