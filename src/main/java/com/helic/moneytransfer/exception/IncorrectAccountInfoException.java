package com.helic.moneytransfer.exception;

public class IncorrectAccountInfoException extends RuntimeException {

    public IncorrectAccountInfoException(String message) {
        super(String.format("Incorrect account information. %s", message));
    }
}
