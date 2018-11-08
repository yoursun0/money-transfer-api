package com.helic.moneytransfer.exception;

public class NotEnoughMoneyException extends RuntimeException {

    private Long accountNo;

    public NotEnoughMoneyException(Long accountNo) {
        super(String.format("Not enough money to transfer from account. [accountNo:%d]", accountNo));
        this.accountNo = accountNo;
    }

    public Long getAccountNo() {
        return accountNo;
    }
}
