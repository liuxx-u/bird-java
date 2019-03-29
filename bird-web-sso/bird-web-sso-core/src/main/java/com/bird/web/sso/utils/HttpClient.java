package com.bird.web.sso.utils;

import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@Slf4j
public class HttpClient {

    private static final int TIME_OUT_MILLIS = 10000;
    private static final int CONNECT_TIME_OUT_MILLIS = 5000;

    public static final String DEFAULT_CONTENT_TYPE = "UTF-8";

    public static HttpResult httpGet(String url, List<String> headers, Map<String, String> paramValues, String encoding) {

        HttpURLConnection conn = null;
        try {
            String encodedContent = encodingParams(paramValues, encoding);
            url += (null == encodedContent) ? "" : ("?" + encodedContent);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT_MILLIS);
            conn.setReadTimeout(TIME_OUT_MILLIS);
            conn.setRequestMethod("GET");
            setHeaders(conn, headers, encoding);
            conn.connect();

            return getResult(conn);
        } catch (Exception e) {
            return new HttpResult(500, e.toString(), Collections.<String, String>emptyMap());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static HttpResult httpPost(String url, Map<String, String> headers, String content) {
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setConnectionTimeToLive(500, TimeUnit.MILLISECONDS);

            CloseableHttpClient httpClient = builder.build();
            HttpPost httpost = new HttpPost(url);

            if(headers != null){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            httpost.setEntity(new StringEntity(content, ContentType.create("application/json", DEFAULT_CONTENT_TYPE)));
            HttpResponse response = httpClient.execute(httpost);
            HttpEntity entity = response.getEntity();

            Reader reader = new InputStreamReader(entity.getContent(), DEFAULT_CONTENT_TYPE);
            CharArrayWriter sw = new CharArrayWriter();
            copy(reader, sw);
            return new HttpResult(response.getStatusLine().getStatusCode(), sw.toString(), Collections.emptyMap());
        } catch (Exception e) {
            return new HttpResult(500, e.toString(), Collections.emptyMap());
        }
    }

    private static HttpResult getResult(HttpURLConnection conn) throws IOException {
        int respCode = conn.getResponseCode();

        InputStream inputStream;
        if (HttpURLConnection.HTTP_OK == respCode || HttpURLConnection.HTTP_NOT_MODIFIED == respCode) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        Map<String, String> respHeaders = new HashMap<String, String>(conn.getHeaderFields().size());
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            respHeaders.put(entry.getKey(), entry.getValue().get(0));
        }

        String encodingGzip = "gzip";

        if (encodingGzip.equals(respHeaders.get(HttpHeaders.CONTENT_ENCODING))) {
            inputStream = new GZIPInputStream(inputStream);
        }

        Reader reader = new InputStreamReader(inputStream, getCharset(conn));
        CharArrayWriter sw = new CharArrayWriter();
        copy(reader, sw);
        return new HttpResult(respCode, sw.toString(), respHeaders);
    }

    private static String getCharset(HttpURLConnection conn) {
        String contentType = conn.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            return DEFAULT_CONTENT_TYPE;
        }

        String[] values = contentType.split(";");
        if (values.length == 0) {
            return DEFAULT_CONTENT_TYPE;
        }

        String charset = DEFAULT_CONTENT_TYPE;
        for (String value : values) {
            value = value.trim();

            if (value.toLowerCase().startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }

        return charset;
    }


    private static void setHeaders(HttpURLConnection conn, List<String> headers, String encoding) {
        if (null != headers) {
            for (Iterator<String> iterator = headers.iterator(); iterator.hasNext(); ) {
                conn.addRequestProperty(iterator.next(), iterator.next());
            }
        }

        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="
                + encoding);
        conn.addRequestProperty("Accept-Charset", encoding);
    }

    private static String encodingParams(Map<String, String> params, String encoding)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (null == params || params.isEmpty()) {
            return null;
        }

        params.put("encoding", encoding);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }

            sb.append(entry.getKey()).append("=");
            sb.append(URLEncoder.encode(entry.getValue(), encoding));
            sb.append("&");
        }

        return sb.toString();
    }

    private static long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1 << 12];
        long count = 0;
        for (int n; (n = input.read(buffer)) >= 0; ) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    public static class HttpResult {
        public final int code;
        public final String content;
        public final Map<String, String> respHeaders;

        HttpResult(int code, String content, Map<String, String> respHeaders) {
            this.code = code;
            this.content = content;
            this.respHeaders = respHeaders;
        }
    }
}
