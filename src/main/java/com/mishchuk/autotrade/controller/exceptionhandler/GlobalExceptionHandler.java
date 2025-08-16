package com.mishchuk.autotrade.controller.exceptionhandler;

import com.mishchuk.autotrade.exception.UserIsBlockedException;
import com.mishchuk.autotrade.exception.AccountNotActiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserIsBlockedException.class)
    public ResponseEntity<Map<String, String>> handleUserNotActive(UserIsBlockedException e) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", "USER_NOT_ACTIVE");
        String message = (e.getMessage() != null) ? e.getMessage() : "User is not activated";
        body.put("message", message);

        log.warn("Login blocked: user not active");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotActive(AccountNotActiveException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", "ACCOUNT_NOT_ACTIVE");
        String message = (e.getMessage() != null) ? e.getMessage() : "Account is not activated";
        body.put("message", message);

        log.warn("Operation blocked: account not active");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}