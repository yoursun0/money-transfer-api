package com.helic.moneytransfer.service;

import java.util.HashMap;
import java.util.Map;

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

    public AccountBalance executeTransactionAndRetrieveBalance(Transaction transaction){
        return retrieveBalanceByRestCall(transaction.getFromAccountNo());
    }

    private AccountBalance retrieveBalanceByRestCall(Long accountNo){
        RestTemplate template = new RestTemplate();

        String uri = "http://localhost:8080/v1/money/account/{accountNo}";

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("accountNo", Long.toString(accountNo));
        AccountBalance response;
        try{
            ResponseEntity<AccountBalance> responseEntity = template.getForEntity(uri, AccountBalance.class, requestMap);
            response = responseEntity.getBody();
        }
        catch(Exception e){
            logger.error(e.getMessage() ,e);
            response = null;
        }
        return response;
    }
}
