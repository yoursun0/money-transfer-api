package com.helic.moneytransfer.service;

import java.util.HashMap;
import java.util.Map;

import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.Transaction;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public void executeTransaction(Transaction transaction) throws AccountNotFoundException {
        logger.info("Execute money transaction: {}", transaction);

        if (isValid(transaction)){
            transfer(transaction);
        }
    }

    private void transfer(Transaction transaction) {

    }

    private boolean isValid(Transaction transaction) throws AccountNotFoundException{
        Long fromAccountNo = transaction.getFromAccountNo();
        Long toAccountNo = transaction.getToAccountNo();
        Account fromAccount = accountRepository.findById(fromAccountNo).orElseThrow(
                () -> new AccountNotFoundException(fromAccountNo)
        );
        Account toAccount = accountRepository.findById(toAccountNo).orElseThrow(
                () -> new AccountNotFoundException(toAccountNo)
        );

        return fromAccount.getBalance() >= transaction.getAmount() && transaction.getToAccountName().equals(toAccount.getName());
    }
}
