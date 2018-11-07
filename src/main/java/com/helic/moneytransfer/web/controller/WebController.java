package com.helic.moneytransfer.web.controller;

import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.service.AccountBalanceService;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * RESTful APIs entry point
 *
 * @author Helic Leung
 */

@RestController
@RequestMapping("/v1/money")
public class WebController {

    private Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private AccountBalanceService accountBalanceService;

    @GetMapping(value = "/account/{accountNo}")
    public AccountBalance getAccount(@PathVariable Long accountNo) {
        logger.info("Get Account API request received.[account No:{}]", accountNo);
        AccountBalance balance = accountBalanceService.getAccountBalanceByAccountNo(accountNo);
        logger.debug("Account balance = {}", balance);
        return balance;
    }

    /**
     * Exception handler for request account ID not found
     *
     * @return HTTP response of status 404 Not Found
     */
    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onAccountNotFound(AccountNotFoundException e) {
        logger.warn("Account not found in database! [account No:{}]", e.getAccountNo());
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorMessage(e.getMessage());
        resp.setErrorCode(HttpStatus.NOT_FOUND.value());
        return resp;
    }
}
