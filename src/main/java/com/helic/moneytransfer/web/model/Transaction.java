package com.helic.moneytransfer.web.model;

import javax.validation.constraints.NotNull;

public class Transaction {

    @NotNull
    private Long fromAccountNo;

    @NotNull
    private Long toAccountNo;

    @NotNull
    private String toAccountName;

    @NotNull
    private double amount;

    private Currency currency;

    private String date;

    public Long getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(Long fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public Long getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(Long toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccountNo=" + fromAccountNo +
                ", toAccountNo=" + toAccountNo +
                ", toAccountName='" + toAccountName + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
