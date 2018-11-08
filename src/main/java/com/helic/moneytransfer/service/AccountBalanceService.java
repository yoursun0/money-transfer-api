package com.helic.moneytransfer.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.Transaction;

@Service
public class AccountBalanceService {

    @Autowired
    private AccountRepository accountRepository;

    RestTemplate restTemplate = new RestTemplate();

    private Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);

    public AccountBalance getAccountBalanceByAccountNo(Long accountNo) throws AccountNotFoundException {
        logger.debug("Getting account information of [accountNo:{}] from database", accountNo);
        Account account = accountRepository.findById(accountNo).orElseThrow(
                () -> new AccountNotFoundException(accountNo)
        );
        return mapToAccountBalance(account);
    }

    public AccountBalance routeCheckBalance(Transaction transaction, String url) {
        // Display the account balance of the from account
        String fromAccountNo = Long.toString(transaction.getFromAccountNo());
        logger.info("Display the account balance of the from account [accountNo:{}]", fromAccountNo);

        String uri = StringUtils.replace(url, "/transaction", "/account/{accountNo}");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("accountNo", fromAccountNo);
        logger.info("Call check balance API by uri:{}", uri);
        return restTemplate.getForObject(uri, AccountBalance.class, requestMap);
    }

    AccountBalance mapToAccountBalance(Account src) {
        AccountBalance dest = new AccountBalance();
        dest.setAccountNo(src.getId());
        dest.setAccountName(src.getName());
        dest.setBalance(src.getBalance());
        dest.setCurrency(src.getCurrency());
        dest.setDateTime(LocalDateTime.now());
        return dest;
    }
}
