package com.helic.moneytransfer.exception;

public class NotSupportedCurrencyException extends RuntimeException {

    private String currency;

    public NotSupportedCurrencyException(String currency) {
        super(String.format("The currency [%s] is not supported by the money transfer system.", currency));
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
