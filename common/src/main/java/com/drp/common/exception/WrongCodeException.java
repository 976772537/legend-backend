package com.drp.common.exception;

/**
 * @author dongruipeng
 * @Descrpition when the code's logic exception throw this exception
 * @date 2019year 12month04day  15:28:47
 */
public class WrongCodeException extends RuntimeException{
    public WrongCodeException(String message) {
        super(message);
    }

    public WrongCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
