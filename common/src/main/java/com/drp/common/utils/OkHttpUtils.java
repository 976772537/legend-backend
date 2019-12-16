package com.drp.common.utils;


import cn.hutool.json.JSONUtil;
import com.drp.common.exception.WrongCodeException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  19:55:12
 */
@Slf4j
@Component
public class OkHttpUtils {

    private final OkHttpClient okHttpClient;

    @Autowired
    public OkHttpUtils(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * http noBodyRequest
     *
     * @param queries url?xxxx=???&xxx=???
     */
    public String get(String url, Headers headers, Map<String, String> queries) {
        url = getFullUrl(url, queries);
        return getHttp(url, headers);
    }

    public String noBodyRequest(String url, Headers headers) {
        return getHttp(url, headers);
    }

    private String getHttp(String url, Headers headers) {
        Request request = new Request.Builder().url(url).headers(headers).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            return requireNonNull(response.body(), "response body is null").string();
        } catch (Exception e) {
            throw new WrongCodeException("http noBodyRequest msg  :" + e.getMessage(), e);
        }
    }

    public String bodyRequest(HttpMethod method,
                              String url,
                              Headers headers,
                              RequestBody requestBody) {

        Request request = requestSelector(method, url, headers, requestBody);
        try (Response response = okHttpClient.newCall(request).execute()) {
            return requireNonNull(response.body(), "response body is null").string();
        } catch (Exception e) {
            throw new WrongCodeException("http " + method.name()
                    + " error msg " + e.getMessage(), e);
        }
    }

    private Request requestSelector(HttpMethod method,
                                    String url,
                                    Headers headers,
                                    RequestBody requestBody) {
        final Request.Builder requestUrl = new Request.Builder()
                .url(url).headers(headers);
        if (method.matches("POST")) {
            return requestUrl.post(requestBody).build();
        }

        if (method.matches("PUT")) {
            return requestUrl.put(requestBody).build();
        }

        if (method.matches("DELETE")) {
            return requestUrl.delete(requestBody).build();
        }
        throw new WrongCodeException("no type for choices");
    }

    public RequestBody getRequestBody(Map<String, String> params, String type) {
        RequestBody requestBody = null;
        if (type.equalsIgnoreCase("form")) {
            FormBody.Builder builder = new FormBody.Builder();
            //add params
            if (params != null && params.keySet().size() > 0) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            requestBody = builder.build();
        }

        if (type.equalsIgnoreCase("json")) {
            requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    JSONUtil.toJsonStr(params)
            );
        }
        return requestBody;
    }

    public RequestBody getRequestBody(String contenType, byte[] body) {
        return RequestBody.create(MediaType.parse(contenType), body);
    }

    private String getFullUrl(String url, Map<String, String> queries) {
        StringBuilder fullUrlBuilder = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {

            boolean firstFlag = true;
            for (Map.Entry entry : queries.entrySet()) {
                if (firstFlag) {
                    fullUrlBuilder.append("?").append(entry.getKey())
                            .append("=").append(entry.getValue());

                    firstFlag = false;
                } else {
                    fullUrlBuilder.append("&").append(entry.getKey())
                            .append("=").append(entry.getValue());
                }
            }
        }
        return fullUrlBuilder.toString();
    }

}
