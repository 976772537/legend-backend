package com.drp.shield.core.trace;

import cn.hutool.core.util.IdUtil;
import com.drp.shield.config.ShieldProperties;
import com.drp.shield.core.http.IncomingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  15:55:14
 */
@Component
public class ProxyingTraceInterceptor {

    private final TraceInterceptor traceInterceptor;
    private final ShieldProperties shieldProperties;

    @Autowired
    public ProxyingTraceInterceptor(TraceInterceptor traceInterceptor,
                                    ShieldProperties shieldProperties) {
        this.traceInterceptor = traceInterceptor;
        this.shieldProperties = shieldProperties;
    }

    public String generateTraceId() {
        return IdUtil.simpleUUID();
    }

    public void onRequestReceived(String traceId,
                                  HttpMethod method,
                                  String originHost,
                                  String originUri,
                                  HttpHeaders headers) {

        runIfTracingIsEnabled(() -> {
            // incoming request
            final IncomingRequest received =
                    new IncomingRequest(method, originHost, originUri, headers);
            traceInterceptor.onRequestReceived(traceId, received);
        });
    }

    public void noMappingFound(String traceId,
                               HttpMethod method,
                               String originHost,
                               String originUri,
                               HttpHeaders headers) {

        runIfTracingIsEnabled(() -> {
            // incoming request
            final IncomingRequest received =
                    new IncomingRequest(method, originHost, originUri, headers);
            traceInterceptor.onNoMappingFound(traceId, received);
        });
    }

    public void onForwardStart(String traceId, String url, int bodyLen) {
        runIfTracingIsEnabled(() -> {
            traceInterceptor.onForwardStart(traceId, url, bodyLen);
        });
    }

    private void runIfTracingIsEnabled(Runnable operation) {
        if (shieldProperties.getTracing().isEnabled()) {
            operation.run();
        }
    }
}
