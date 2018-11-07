package com.helic.moneytransfer.exception;

public class WrongRequestFormatException extends RuntimeException {

    public WrongRequestFormatException(String message) {
        super(String.format("Unexpected request format. %s", message));
    }
}
