package com.helic.moneytransfer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helic.moneytransfer.db.dao.TransactionDAO;
import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.exception.IncorrectAccountInfoException;
import com.helic.moneytransfer.exception.TransferAmountNotPositiveException;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;
import com.helic.moneytransfer.exception.NotSupportedCurrencyException;
import com.helic.moneytransfer.web.model.Transaction;

@Service
public class TransactionService {

    private static final String HKD = "HKD";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDAO transactionDAO;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public void executeTransaction(Transaction transaction) {
        logger.info("Execute money transaction: {}", transaction);
        validateTransaction(transaction);
        transferMoney(transaction);
    }

    private void transferMoney(Transaction transaction) {
        logger.info("Transfer money ${} from account No:{} to account No:{}", transaction.getAmount(), transaction.getFromAccountNo(), transaction.getToAccountNo());
        transactionDAO.transferMoney(transaction.getFromAccountNo(), transaction.getToAccountNo(), transaction.getAmount());
    }

    private void validateTransaction(Transaction transaction) {
        Long fromAccountNo = transaction.getFromAccountNo();
        Long toAccountNo = transaction.getToAccountNo();

        // Only HKD is supported for the current version
        if (!HKD.equals(transaction.getCurrency().toUpperCase())){
            throw new NotSupportedCurrencyException(transaction.getCurrency());
        }

        // Transfer amount cannot be negative
        if (transaction.getAmount() <= 0){
            throw new TransferAmountNotPositiveException(transaction.getAmount());
        }

        if (fromAccountNo == toAccountNo){
            throw new IncorrectAccountInfoException("The transaction from account and to account cannot be the same.");
        }

        Account fromAccount = accountRepository.findById(fromAccountNo).orElseThrow(
                () -> new AccountNotFoundException(fromAccountNo)
        );
        Account toAccount = accountRepository.findById(toAccountNo).orElseThrow(
                () -> new AccountNotFoundException(toAccountNo)
        );

        if (!transaction.getToAccountName().equals(toAccount.getName())) {
            throw new IncorrectAccountInfoException("Account holder name of the input is incorrect.");
        }

        if (fromAccount.getBalance() < transaction.getAmount()) {
            throw new NotEnoughMoneyException(fromAccountNo, transaction.getAmount());
        }
    }
}
