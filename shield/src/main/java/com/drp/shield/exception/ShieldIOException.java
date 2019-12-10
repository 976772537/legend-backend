package com.drp.shield.exception;

import java.io.IOException;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month09day  15:48:20
 */
public class ShieldIOException extends RuntimeException {
    public ShieldIOException() {
    }

    public ShieldIOException(String message) {
        super(message);
    }

    public ShieldIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
