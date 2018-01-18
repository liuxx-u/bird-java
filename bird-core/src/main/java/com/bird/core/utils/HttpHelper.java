package com.bird.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liuxx on 2017/7/12.
 */
public class HttpHelper {
    private final static Logger logger = LoggerFactory.getLogger("httpHelper");

    public HttpHelper() {

    }

    /**
     * httppost请求(PostMethod)
     *
     * @param url
     * @param data
     * @return String
     * @author liuxx
     */
    public static final <T extends Object> T postJson(String url,String token, Object data, Class<T> clazz) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        if(!StringUtils.isBlank(token)){
            post.setHeader("sso.token", token);
        }
        T result;
        try {
            if (data != null) {
                StringEntity s = new StringEntity(JSON.toJSONString(data), "utf-8");
                s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(s);
            }

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));

            String response;
            StringBuilder sbResponse = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) sbResponse.append(line + "\n");
            inStream.close();

            response = sbResponse.toString();
            System.out.println(response);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = JSON.parseObject(response, clazz);
                System.out.println("请求服务器成功，做相应处理");

            } else {
                result = null;
                System.out.println("请求服务端失败");
            }
        } catch (Exception e) {
            System.out.println("请求异常");
            throw new RuntimeException(e);
        }

        return result;
    }
}
