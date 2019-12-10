package com.drp.shield.core.trace;

import com.drp.shield.core.http.IncomingRequest;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  16:48:40
 */
public interface TraceInterceptor {

    void onRequestReceived(String traceId, IncomingRequest received);

    void onNoMappingFound(String traceId, IncomingRequest request);
}
