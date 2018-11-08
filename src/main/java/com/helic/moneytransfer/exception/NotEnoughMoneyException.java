package com.helic.moneytransfer.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(long accountNo, double amount) {
        super(String.format("Not enough money to transfer [%.2f] from account. [accountNo:%d]", amount, accountNo));
    }
}
