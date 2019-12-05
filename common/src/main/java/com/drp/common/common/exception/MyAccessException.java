package com.drp.common.common.exception;

/**
 * @author dongruipeng
 * @Descrpition when the USER doesn't have access throw this exception
 * @date 2019year 12month04day  15:33:42
 */
public class MyAccessException extends RuntimeException{

    public MyAccessException(String message) {
        super(message);
    }

    public MyAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
