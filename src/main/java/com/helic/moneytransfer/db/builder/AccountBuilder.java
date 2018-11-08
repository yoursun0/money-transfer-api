package com.helic.moneytransfer.db.builder;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.helic.moneytransfer.db.entity.Account;

public class AccountBuilder implements Serializable {

    private Long id;

    private String name;

    private Double balance;

    private String currency;

    private LocalDateTime createdTs = LocalDateTime.now();

    private LocalDateTime updatedTs = LocalDateTime.now();

    public static AccountBuilder aBuilder = new AccountBuilder();

    public AccountBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public AccountBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AccountBuilder setBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public AccountBuilder setCreatedTs(LocalDateTime createdTs) {
        this.createdTs = createdTs;
        return this;
    }

    public AccountBuilder setUpdatedTs(LocalDateTime updatedTs) {
        this.updatedTs = updatedTs;
        return this;
    }

    public Account build() {
        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setBalance(balance);
        account.setCurrency(currency);
        account.setCreatedTs(createdTs);
        account.setUpdatedTs(updatedTs);
        return account;
    }
}
