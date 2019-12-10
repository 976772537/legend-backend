package com.drp.shield.exception;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month09day  14:28:41
 */
public class WrongConfigureException extends RuntimeException {
    public WrongConfigureException(String message) {
        super(message);
    }

    public WrongConfigureException(String message, Throwable cause) {
        super(message, cause);
    }
}
