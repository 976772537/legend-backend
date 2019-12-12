package com.drp.shield.core.http;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.drp.common.utils.OkHttpUtils;
import com.drp.shield.config.SecurityConfig;
import com.drp.shield.core.trace.ProxyingTraceInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

import static cn.hutool.core.util.ObjectUtil.isNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month12day  15:40:36
 */
@Slf4j
@Component
public class RequestForwarder {
    private final OkHttpUtils okHttpUtils;
    private final ProxyingTraceInterceptor traceInterceptor;
    private final SecurityConfig securityConfig;

    @Autowired
    public RequestForwarder(OkHttpUtils okHttpUtils,
                            ProxyingTraceInterceptor traceInterceptor,
                            SecurityConfig securityConfig) {
        this.okHttpUtils = okHttpUtils;
        this.traceInterceptor = traceInterceptor;
        this.securityConfig = securityConfig;
    }

    /**
     * request send
     * bodyRequest post put delete
     * nobodyRequest get
     */
    public String requestSend(HttpHeaders headers,
                              HttpMethod method,
                              URI url,
                              byte[] body) {
        //bodyRequest http
        if (isNotNull(body) && headers.containsKey(CONTENT_TYPE)) {
            final List<String> contentTypes = headers.get(CONTENT_TYPE);
            //get contentType
            if (contentTypes != null && contentTypes.size() != 0) {
                final RequestBody requestBody =
                        okHttpUtils.getRequestBody(contentTypes.get(0), body);
                final Headers okHeaders = Headers.of(headers.toSingleValueMap());
                return okHttpUtils.bodyRequest(method, url.toString(), okHeaders, requestBody);
            }
        }

        if (ArrayUtil.isEmpty(body)) {
            //http noBodyRequest
            final Headers okHeaders = Headers.of(headers.toSingleValueMap());
            return okHttpUtils.noBodyRequest(url.toString(), okHeaders);
        }
        return null;
    }

    public String forwarderHttpRequest(String traceId,
                                       HttpHeaders headers,
                                       HttpMethod method,
                                       URI url,
                                       byte[] body) {
        traceInterceptor.onForwardStart(traceId, url.toString(), body.length);
        if (securityAuthentication(headers, url)) {
            return requestSend(headers, method, url, body);
        }
        return null;
    }

    private boolean securityAuthentication(HttpHeaders headers, URI url) {
        final List<String> permitPaths = securityConfig.getPermitPaths();
        if (permitPaths.contains(url.getPath())) {
            return true;
        } else {
            if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                final List<String> auths = headers.get(HttpHeaders.AUTHORIZATION);
                return CollectionUtil.isNotEmpty(auths);
            }
            return false;
        }
    }
}
