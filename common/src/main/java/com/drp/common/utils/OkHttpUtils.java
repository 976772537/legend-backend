package com.drp.common.utils;


import cn.hutool.json.JSONUtil;
import com.drp.common.exception.WrongCodeException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  19:55:12
 */
@Slf4j
public class OkHttpUtils {

    @Autowired
    private static OkHttpClient okHttpClient;

    /**
     * http get
     *
     * @param queries url?xxxx=???&xxx=???
     */
    public static String get(String url, Map<String, String> queries) {
        url = getFullUrl(url, queries);
        Request request = new Request.Builder().url(url).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return requireNonNull(response.body(), "response body is null").string();
            }
            throw new WrongCodeException("okHttp3 get error code >> " + response.code());
        } catch (Exception e) {
            throw new WrongCodeException("okHttp3 get error >> " + e.getMessage());
        }
    }

    public static String post(String url, Map<String, String> params, String type) {

        Request request = requestSelector(url, params, type);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return requireNonNull(response.body(), "response body is null").string();
            }
            throw new WrongCodeException("okHttp3 post error code>> " + response.code());
        } catch (Exception e) {
            throw new WrongCodeException("okHttp3 post error >> " + e.getMessage());
        }
    }

    private static Request requestSelector(String url, Map<String, String> params, String type) {
        if (type.equalsIgnoreCase("from")) {
            FormBody.Builder builder = new FormBody.Builder();
            //add params
            if (params != null && params.keySet().size() > 0) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            return new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        }

        if (type.equalsIgnoreCase("json")) {
            final RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    JSONUtil.toJsonStr(params)
            );
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        throw new WrongCodeException("no type for choices");
    }

    private static String getFullUrl(String url, Map<String, String> queries) {
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
