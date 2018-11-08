package com.helic.moneytransfer.db.dao;

import java.time.LocalDateTime;
import javax.transaction.Transactional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.helic.moneytransfer.db.builder.TransactionBuilder;
import com.helic.moneytransfer.db.repo.TransactionRepository;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;

@Repository
public class TransactionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void transferMoney(long fromAccountNo, long toAccountNo, double transferAmount) throws NotEnoughMoneyException {
        MutableBoolean update = new MutableBoolean(false);
        jdbcTemplate.query(
                "SELECT * FROM account WHERE id = ? FOR UPDATE", new Object[]{fromAccountNo}, (rs -> {
                    update.setValue(rs.getDouble("balance") >= transferAmount);
                }));
        if (update.booleanValue()) {

            LocalDateTime timeNow = LocalDateTime.now();
            jdbcTemplate.update("UPDATE account SET updated_ts = ?, balance = balance - ? WHERE id = ?", timeNow, transferAmount, fromAccountNo);
            jdbcTemplate.update("UPDATE account SET updated_ts = ?, balance = balance + ? WHERE id = ?", timeNow, transferAmount, toAccountNo);

            com.helic.moneytransfer.db.entity.Transaction transactionRecord = TransactionBuilder.aBuilder
                    .setFromAccountNo(fromAccountNo)
                    .setToAccountNo(toAccountNo)
                    .setAmount(transferAmount)
                    .setCurrency("HKD")
                    .setCreatedTs(timeNow)
                    .setUpdatedTs(timeNow)
                    .build();

            transactionRepository.save(transactionRecord);

        } else {
            throw new NotEnoughMoneyException(fromAccountNo, transferAmount);
        }
    }
}
