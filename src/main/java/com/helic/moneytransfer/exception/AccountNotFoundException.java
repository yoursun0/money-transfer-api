package com.helic.moneytransfer.exception;

public class AccountNotFoundException extends RuntimeException {

    private String accountId;

    public AccountNotFoundException(String accountId) {
        super(String.format("Account Not Found [accountId:%s]", accountId));
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
