package com.drp.shield.core.filter;

import cn.hutool.core.util.CharsetUtil;
import com.drp.common.utils.OkHttpUtils;
import com.drp.shield.config.MappingProperties;
import com.drp.shield.config.ShieldProperties;
import com.drp.shield.core.http.RequestDataExtractor;
import com.drp.shield.core.trace.ProxyingTraceInterceptor;
import com.drp.shield.exception.ShieldNetException;
import com.drp.shield.exception.WrongConfigureException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.RequestBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.drp.shield.core.http.RequestDataExtractor.extractHost;
import static com.drp.shield.core.http.RequestDataExtractor.extractHttpHeaders;
import static com.drp.shield.core.http.RequestDataExtractor.extractHttpMethod;
import static com.drp.shield.core.http.RequestDataExtractor.extractUri;
import static com.drp.shield.core.http.RequestDataExtractor.getUrl;
import static java.lang.String.valueOf;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  10:04:20
 */
@Slf4j
public class ReverseProxyFilter extends OncePerRequestFilter {

    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    private static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";

    private final ProxyingTraceInterceptor traceInterceptor;
    private final ShieldProperties shieldProperties;
    private final OkHttpUtils okHttpUtils;

    public ReverseProxyFilter(ProxyingTraceInterceptor trace,
                              ShieldProperties shieldProperties,
                              OkHttpUtils okHttpUtils) {
        this.traceInterceptor = trace;
        this.shieldProperties = shieldProperties;
        this.okHttpUtils = okHttpUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //get request detials
        String originUri = extractUri(request);
        String originHost = extractHost(request);
        final HttpHeaders headers = extractHttpHeaders(request);
        final HttpMethod method = extractHttpMethod(request);
        //generate TraceId
        final String traceId = traceInterceptor.generateTraceId();
        //log received
        traceInterceptor.onRequestReceived(traceId, method, originHost, originUri, headers);
        //resolve mappings
        final List<String> destinations = resolveMappings(originHost,
                originUri, shieldProperties.getMappings());

        if (destinations == null) {
            //worng
            traceInterceptor.noMappingFound(traceId, method, originHost, originUri, headers);
            responseWrong(response);
            return;
        }
        //add forward heads
        addForwardHeaders(request, headers);

        final URI url = getUrl(request, destinations.get(0));
        byte[] body = RequestDataExtractor.extractBody(request);
        String reponseBody;
        //common http
        if (body != null && headers.containsKey(CONTENT_TYPE)) {
            final List<String> contentTypes = headers.get(CONTENT_TYPE);
            //get contentType
            if (contentTypes != null && contentTypes.size() > 0) {
                //verify url
                if (url != null) {
                    final RequestBody requestBody =
                            okHttpUtils.getRequestBody(contentTypes.get(0), body);
                    final Headers okHeaders = Headers.of(headers.toSingleValueMap());
                    reponseBody =
                            okHttpUtils.common(method, url.toString(), okHeaders, requestBody);
                    writeResponseBody(response, reponseBody);
                    return;
                } else {
                    responseWrong(response);
                }

            } else {
                throw new ShieldNetException("Unsupport Request Body");
            }

        }
        //get http
        if (body == null || body.length == 0) {
            if (url != null) {
                final Headers okHeaders = Headers.of(headers.toSingleValueMap());
                reponseBody = okHttpUtils.get(url.toString(), okHeaders);
                writeResponseBody(response, reponseBody);
            } else {
                throw new ShieldNetException("get url is null");
            }
        }
    }

    private void writeResponseBody(HttpServletResponse response, String reponseBody) throws IOException {
        if (reponseBody != null) {
            response.setCharacterEncoding(CharsetUtil.UTF_8);
            response.getWriter().write(reponseBody);
        } else {
            throw new ShieldNetException("reponseBody is null");
        }
    }

    private void responseWrong(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("Unsupported domain");
    }

    private void addForwardHeaders(HttpServletRequest request, HttpHeaders headers) {
        List<String> forwardedFor = headers.get(X_FORWARDED_FOR_HEADER);
        if (isEmpty(forwardedFor)) {
            forwardedFor = new ArrayList<>();
        }
        forwardedFor.add(request.getRemoteAddr());
        headers.put(X_FORWARDED_FOR_HEADER, forwardedFor);
        headers.set(X_FORWARDED_PROTO_HEADER, request.getScheme());
        headers.set(X_FORWARDED_HOST_HEADER, request.getServerName());
        headers.set(X_FORWARDED_PORT_HEADER, valueOf(request.getServerPort()));
    }

    /**
     * find destinations by originHost
     */
    private List<String> resolveMappings(String originHost,
                                         String originUri,
                                         List<MappingProperties> mappings) {
        final List<MappingProperties> uriMapList = mappings.stream()
                .filter(map -> map.getHost().equalsIgnoreCase(originHost))
                .filter(map -> map.getUris().stream().anyMatch(originUri::contains))
                .collect(Collectors.toList());
        if (uriMapList.size() > 1) {
            throw new WrongConfigureException("not only uri find");
        }
        return uriMapList.size() != 0 ? uriMapList.get(0).getDestinations() : null;
    }
}