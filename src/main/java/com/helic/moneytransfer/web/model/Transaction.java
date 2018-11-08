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

    // default currency for money transaction is HKD
    private String currency = "HKD";

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccountNo=" + fromAccountNo +
                ", toAccountNo=" + toAccountNo +
                ", toAccountName='" + toAccountName + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
