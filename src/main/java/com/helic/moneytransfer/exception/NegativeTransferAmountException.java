package com.helic.moneytransfer.exception;

public class NegativeTransferAmountException extends RuntimeException {

    public NegativeTransferAmountException(double amount) {
        super(String.format("The transfer amount [%.2f] cannot be negative.", amount));
    }
}
