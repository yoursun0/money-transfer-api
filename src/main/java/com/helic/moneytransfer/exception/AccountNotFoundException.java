package com.helic.moneytransfer.exception;

public class AccountNotFoundException extends RuntimeException {

    private Long accountNo;

    public AccountNotFoundException(Long accountNo) {
        super(String.format("Account Not Found [accountNo:%d]", accountNo));
        this.accountNo = accountNo;
    }

    public Long getAccountNo() {
        return accountNo;
    }
}
