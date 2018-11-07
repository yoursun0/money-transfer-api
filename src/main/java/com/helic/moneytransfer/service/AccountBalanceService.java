package com.helic.moneytransfer.service;

import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.web.model.Currency;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.web.model.AccountBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountBalanceService {

    @Autowired
    private AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);

    public AccountBalance getAccountBalanceByAccountNo(Long accountNo) throws AccountNotFoundException {
        logger.debug("Getting account information of [accountNo:{}] from database", accountNo);
        Account account = accountRepository.findById(accountNo).orElseThrow(
                () -> new AccountNotFoundException(accountNo)
        );
        return mapToAccountBalance(account);
    }

    private AccountBalance mapToAccountBalance(Account src){
        AccountBalance dest = new AccountBalance();
        dest.setAccountNo(src.getId());
        dest.setAccountName(src.getName());
        dest.setBalance(src.getBalance());
        dest.setCurrency(Currency.valueOf(src.getCurrency()));
        dest.setDateTime(LocalDateTime.now());
        return dest;
    }
}
