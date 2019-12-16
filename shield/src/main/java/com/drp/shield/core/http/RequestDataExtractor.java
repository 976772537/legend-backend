package com.drp.shield.core.http;

import cn.hutool.core.io.IoUtil;
import com.drp.shield.exception.ShieldIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  10:24:50
 */
@Slf4j
public class RequestDataExtractor {
    public static String extractUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static String getQuery(HttpServletRequest request) {
        return request.getQueryString();
    }

    public static String extractHost(HttpServletRequest request) {
        return request.getServerName();
    }

    public static HttpHeaders extractHttpHeaders(HttpServletRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            final List<String> value = Collections.list(request.getHeaders(name));
            headers.put(name, value);
        }
        return headers;
    }

    public static HttpMethod extractHttpMethod(HttpServletRequest request) {
        return HttpMethod.resolve(request.getMethod());
    }

    public static URI getUrl(HttpServletRequest request, String destinations) {
        return getUrl(request.getScheme(),
                request.getRequestURI(),
                destinations,
                getQuery(request));
    }

    public static URI getUrl(String scheme, String url, String destinations, String queryStr) {
        final String[] des = destinations.split(":");
        try {
            return new URI(scheme,
                    null,
                    des[0],
                    Integer.parseInt(des[1]),
                    url,
                    queryStr, null);
        } catch (URISyntaxException e) {
            log.error("Fail to build redirect url", e);
            return null;
        }
    }

    public static byte[] extractBody(HttpServletRequest request) {
        try {
            return IoUtil.readBytes(request.getInputStream());
        } catch (IOException e) {
            throw new ShieldIOException("Error extracting body of HTTP request with URI: "
                    + extractUri(request), e);
        }
    }
}
