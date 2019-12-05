package com.drp.common.utils;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  15:08:28
 */
public final class NetworkResult<T> {

    public static final String BAD_REQUEST_ERROR_MSG = "Bad request!";

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private T data;


    /**
     * constructor for a GOOD result
     */
    public NetworkResult(T data) {
        code = HttpServletResponse.SC_OK;
        this.data = data;
    }

    public NetworkResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

}
