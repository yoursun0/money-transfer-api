package com.helic.moneytransfer.exception;

public class TransferAmountNotPositiveException extends RuntimeException {

    public TransferAmountNotPositiveException(double amount) {
        super(String.format("The transfer amount [%.2f] must be positive number.", amount));
    }
}
