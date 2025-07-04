package com.mishchuk.autotrade.exception;

public class UserIsBlockedException extends RuntimeException {
    public UserIsBlockedException(String message) {
        super(message);
    }
}
