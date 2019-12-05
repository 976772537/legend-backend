package com.drp.sso.sso.common.utils;


import com.drp.sso.sso.common.exception.MyAccessException;
import com.drp.sso.sso.common.exception.WrongCodeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * @author dongruipeng
 * @Descrpition monitor log can use this class
 * @date 2019year 12month04day  15:05:08
 */
public final class RsHelper<T> {

    public static <T> NetworkResult<T> success(T data) {
        return new NetworkResult<T>(data);
    }

    public static <T> ResponseEntity<NetworkResult<T>> error(Throwable throwable) {
        int code = getErrorCode(throwable);
        final NetworkResult<T> ns = new NetworkResult<>(code, getExceptionMessage(throwable));
        return response(ns);
    }

    private static int getErrorCode(Throwable throwable) {
        final Class<? extends Throwable> ex = throwable.getClass();
        if (ex == WrongCodeException.class) {
            return 500;
        } else if (ex == MyAccessException.class) {
            return 403;
        }
        return 400;
    }

    /**
     * httpStatus aways ok
     */
    private static <T> ResponseEntity<T> response(T body) {
        return new ResponseEntity<T>(body, new HttpHeaders(), HttpStatus.OK);
    }

    private static String getExceptionMessage(Throwable throwable) {
        return throwable.getMessage() == null ? NetworkResult.BAD_REQUEST_ERROR_MSG : throwable.getMessage();
    }
}
