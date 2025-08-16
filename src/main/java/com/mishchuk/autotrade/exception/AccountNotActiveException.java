package com.mishchuk.autotrade.exception;

public class AccountNotActiveException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Account is not activated";

    public AccountNotActiveException() {
        super(DEFAULT_MESSAGE);
    }

    public AccountNotActiveException(String message) {
        super(message);
    }
}