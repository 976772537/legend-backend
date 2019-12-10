package com.drp.shield.core.trace;

import cn.hutool.json.JSONUtil;
import com.drp.shield.core.http.IncomingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  16:58:37
 */
@Slf4j
@Component
public class LoggingTraceInterceptor implements TraceInterceptor {
    @Override
    public void onRequestReceived(String traceId, IncomingRequest request) {
        log.info(JSONUtil.toJsonStr(new HashMap<String, Object>() {{
            put("type", "Incoming HTTP request received");
            put("traceId", traceId);
            put("request", request);
        }}));
    }

    @Override
    public void onNoMappingFound(String traceId, IncomingRequest request) {
        log.info(JSONUtil.toJsonStr(new HashMap<String, Object>() {{
            put("type", "No mapping found for incoming HTTP request");
            put("traceId", traceId);
            put("request", request);
        }}));
    }
}
