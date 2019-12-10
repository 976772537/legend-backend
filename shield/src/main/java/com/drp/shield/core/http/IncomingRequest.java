package com.drp.shield.core.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month09day  09:53:45
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingRequest {

    private HttpMethod method;
    private String originHost;
    private String originUri;
    private HttpHeaders headers;

}
