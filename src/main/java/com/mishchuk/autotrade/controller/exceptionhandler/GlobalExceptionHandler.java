package com.mishchuk.autotrade.controller.exceptionhandler;

import com.mishchuk.autotrade.exception.UserIsBlockedException;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIsBlockedException.class)
    public ResponseEntity<Void> handleUserIsBlockedException(UserIsBlockedException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
