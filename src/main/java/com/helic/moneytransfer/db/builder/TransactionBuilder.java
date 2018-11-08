package com.helic.moneytransfer.db.builder;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.helic.moneytransfer.db.entity.Transaction;

public class TransactionBuilder implements Serializable {

    private Long id;

    private Long fromAccountNo;

    private Long toAccountNo;

    private Double amount;

    private String currency;

    private LocalDateTime createdTs;

    private LocalDateTime updatedTs;

    public static TransactionBuilder aBuilder = new TransactionBuilder();

    public TransactionBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder setFromAccountNo(Long fromAccountNo){
        this.fromAccountNo = fromAccountNo;
        return this;
    }

    public TransactionBuilder setToAccountNo(Long toAccountNo){
        this.toAccountNo = toAccountNo;
        return this;
    }

    public TransactionBuilder setAmount(Double amount){
        this.amount = amount;
        return this;
    }

    public TransactionBuilder setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public TransactionBuilder setCreatedTs(LocalDateTime createdTs) {
        this.createdTs = createdTs;
        return this;
    }

    public TransactionBuilder setUpdatedTs(LocalDateTime updatedTs) {
        this.updatedTs = updatedTs;
        return this;
    }

    public Transaction build() {
        Transaction Transaction = new Transaction();
        Transaction.setId(id);
        Transaction.setFromAccountNo(fromAccountNo);
        Transaction.setToAccountNo(toAccountNo);
        Transaction.setAmount(amount);
        Transaction.setCurrency(currency);
        Transaction.setCreatedTs(createdTs);
        Transaction.setUpdatedTs(updatedTs);
        return Transaction;
    }
}
