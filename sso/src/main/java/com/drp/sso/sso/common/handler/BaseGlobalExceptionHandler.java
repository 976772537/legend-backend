package com.drp.sso.sso.common.handler;

import com.drp.sso.sso.common.utils.RsHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author dongruipeng
 * @Descrpition controller advice
 * @date 2019year 12month04day  14:40:18
 */
@ControllerAdvice
public class BaseGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<?> handleException(Exception ex) {
        return RsHelper.error(ex);
    }
}
