package com.java3y.austin.support.utils;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author 3y
 * @date 2021/11/4
 */
@Slf4j
@Component
public class OkHttpUtils {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }


    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGetWithHeaders(String url, Map<String, String> headers) {
        return doGet(url, null, headers);
    }


    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        if (Objects.nonNull(params) && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(sb.toString()).build();

        log.info("do get request and url[{}]", sb);
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPost(String url, Map<String, String> params, Map<String, String> headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (Objects.nonNull(params) && params.keySet().size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder builder = getBuilderWithHeaders(headers);

        Request request = builder.url(url).post(formBuilder.build()).build();
        log.info("do post request and url[{}]", url);

        return execute(request);
    }


    /**
     * 获取request Builder
     *
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return
     */
    private Request.Builder getBuilderWithHeaders(Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (!MapUtil.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, null);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPostJsonWithHeaders(String url, String json, Map<String, String> headers) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, headers);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return executePost(url, xml, XML, null);
    }


    private String executePost(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(data.getBytes(StandardCharsets.UTF_8), contentType);
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return String.valueOf(response.body());
            }
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
        return "";
    }

}

