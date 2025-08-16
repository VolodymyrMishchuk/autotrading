package com.mishchuk.autotrade.exception;

public class UserIsBlockedException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "User is not activated";

    public UserIsBlockedException() {
        super(DEFAULT_MESSAGE);
    }

    public UserIsBlockedException(String message) {
        super(message);
    }
}
