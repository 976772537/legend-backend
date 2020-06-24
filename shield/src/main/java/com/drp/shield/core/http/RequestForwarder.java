package com.drp.shield.core.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.drp.common.ActionUri;
import com.drp.common.Const;
import com.drp.common.RootUri;
import com.drp.common.utils.DrpUtils;
import com.drp.common.utils.NetworkResult;
import com.drp.common.utils.OkHttpUtils;
import com.drp.shield.config.MappingProperties;
import com.drp.shield.config.SecurityConfig;
import com.drp.shield.config.ShieldProperties;
import com.drp.shield.core.trace.ProxyingTraceInterceptor;
import com.drp.shield.exception.WrongConfigureException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.isNotNull;
import static com.drp.shield.core.http.RequestDataExtractor.getUrl;
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
    private final ShieldProperties shieldProperties;

    @Autowired
    public RequestForwarder(OkHttpUtils okHttpUtils,
                            ProxyingTraceInterceptor traceInterceptor,
                            SecurityConfig securityConfig, ShieldProperties shieldProperties) {
        this.okHttpUtils = okHttpUtils;
        this.traceInterceptor = traceInterceptor;
        this.securityConfig = securityConfig;
        this.shieldProperties = shieldProperties;
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
        final int authCode = securityAuthentication(headers, url);
        if (authCode == 200) {
            return requestSend(headers, method, url, body);
        }
        final NetworkResult<String> net = new NetworkResult<>(authCode,
                org.springframework.http.HttpStatus.valueOf(authCode).toString());
        return JSONUtil.toJsonStr(net);
    }

    private int securityAuthentication(HttpHeaders headers, URI url) {
        final Set<String> permitPaths = securityConfig.getPermitPaths();
        if (verifySecurityPath(permitPaths, url.getPath())) {
            return HttpStatus.HTTP_OK;
        }
        // get authentication token
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            return HttpStatus.HTTP_UNAUTHORIZED;
        }

        final List<String> auths = headers.get(HttpHeaders.AUTHORIZATION);
        if (auths == null || auths.size() == 0) {
            return HttpStatus.HTTP_UNAUTHORIZED;
        }

        final List<MappingProperties> proList = getSsoProperties();
        final String destinations = LoadBalance.average(proList.get(0).getDestinations());

        URI ssoUrl = getUrl("http", "/" + RootUri.USER + ActionUri.AUTH_TOKEN,
                destinations, "token=" + auths.get(0));

        final String result = requestSend(headers, null, ssoUrl, null);
        try {
            if (isNotNull(result)) {
                return Integer.parseInt(result);
            }
        } catch (NumberFormatException e) {
            log.info("illegality request attack ip : " + DrpUtils.getIpAddr(headers));
            return HttpStatus.HTTP_FORBIDDEN;
        }
        return HttpStatus.HTTP_BAD_REQUEST;
    }

    /**
     * get Sso Properties
     */
    private List<MappingProperties> getSsoProperties() {
        final List<MappingProperties> proList =
                shieldProperties.getMappings()
                        .stream()
                        .filter(pro -> pro.getName().equals(Const.USER_SSO_SERVER))
                        .collect(Collectors.toList());
        if (proList.size() != 1) {
            throw new WrongConfigureException("common has mappings name [sso] num => "
                    + proList.size());
        }
        return proList;
    }

    private boolean verifySecurityPath(Set<String> permitPaths, String path) {
        if (permitPaths.contains(path)) return true;
        final String[] reqPath = path.split("/");
        StringBuilder patternBuilder = new StringBuilder().append("^").append(reqPath[1]);
        for (int i = 2; i < reqPath.length; i++) {
            patternBuilder.append("/(\\*|").append(reqPath[i]).append(")");
        }
        String pattern = patternBuilder.toString();

        for (String secPath : permitPaths) {
            if (secPath.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
