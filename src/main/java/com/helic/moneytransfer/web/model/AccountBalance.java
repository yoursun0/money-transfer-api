package com.helic.moneytransfer.web.model;

import com.helic.moneytransfer.db.entity.Currency;

import java.time.LocalDateTime;

public class AccountBalance {

    private Long accountNo;

    private String accountName;

    private double balance;

    private Currency currency;

    private LocalDateTime dateTime;

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "accountNo='" + accountNo + '\'' +
                ", accountName='" + accountName + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                ", dateTime=" + dateTime +
                '}';
    }
}
