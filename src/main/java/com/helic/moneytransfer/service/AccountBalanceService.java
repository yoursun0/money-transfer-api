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

@Service
public class AccountBalanceService {

    @Autowired
    private AccountRepository accountRepository;

    RestTemplate restTemplate = new RestTemplate();

    private Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);

    /**
     * Get account information in AccountBalance object format, by providing account number
     *
     * @param accountNo the account number
     * @return an AccountBalance object containing key information of the account, including account holder name, currency, balance, etc.
     */
    public AccountBalance getAccountBalanceByAccountNo(Long accountNo) throws AccountNotFoundException {
        logger.debug("Getting account information of [accountNo:{}] from database", accountNo);
        Account account = accountRepository.findById(accountNo).orElseThrow(
                () -> new AccountNotFoundException(accountNo)
        );
        return mapToAccountBalance(account);
    }

    /**
     * Call the GET Account Info API by another REST call, to retrieve latest account balance
     *
     * @param accountNo the account number
     * @return an AccountBalance object containing key information of the account, including account holder name, currency, balance, etc.
     */
    public AccountBalance routeCheckBalance(Long accountNo, String url) {
        // Display the account balance of the from account
        String fromAccountNo = Long.toString(accountNo);
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
