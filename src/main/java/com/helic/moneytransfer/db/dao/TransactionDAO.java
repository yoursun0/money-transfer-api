package com.helic.moneytransfer.db.dao;

import javax.transaction.Transactional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.helic.moneytransfer.exception.NotEnoughMoneyException;

@Repository
public class TransactionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void transferMoney(long fromAccountNo, long toAccountNo, double transferAmount) throws NotEnoughMoneyException {
        MutableBoolean update = new MutableBoolean(false);
        jdbcTemplate.query(
                "SELECT * FROM account WHERE id = ? FOR UPDATE", new Object[]{fromAccountNo}, (rs -> {
                    update.setValue(rs.getDouble("balance") >= transferAmount);
                }));
        if (update.booleanValue()) {
            jdbcTemplate.update("UPDATE account SET balance = balance - ? WHERE id = ?", transferAmount, fromAccountNo);
            jdbcTemplate.update("UPDATE account SET balance = balance + ? WHERE id = ?", transferAmount, toAccountNo);
        } else {
            throw new NotEnoughMoneyException(fromAccountNo);
        }
    }
}
