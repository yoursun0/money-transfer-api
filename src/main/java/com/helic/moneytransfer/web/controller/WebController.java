package com.helic.moneytransfer.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.exception.IncorrectAccountInfoException;
import com.helic.moneytransfer.exception.TransferAmountNotPositiveException;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;
import com.helic.moneytransfer.exception.NotSupportedCurrencyException;
import com.helic.moneytransfer.exception.WrongRequestFormatException;
import com.helic.moneytransfer.service.AccountBalanceService;
import com.helic.moneytransfer.service.TransactionService;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.ErrorResponse;
import com.helic.moneytransfer.web.model.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/account/{accountNo}")
    public @ResponseBody
    AccountBalance getAccount(@PathVariable String accountNo) {
        logger.info("Get Account API request received.[account No:{}]", accountNo);
        try {
            Long accountNumber = Long.valueOf(accountNo);
            AccountBalance balance = accountBalanceService.getAccountBalanceByAccountNo(accountNumber);
            logger.debug("Account balance = {}", balance);
            return balance;
        } catch (NumberFormatException ex) {
            throw new WrongRequestFormatException("Account No must be an integer.");
        }
    }

    @PostMapping(value = "/transaction")
    public @ResponseBody
    AccountBalance executeTransaction(
            HttpServletRequest request,
            @Valid @RequestBody Transaction transaction, BindingResult bindingResult) throws WrongRequestFormatException, AccountNotFoundException {

        logger.info("Execute Transaction API request received.");

        // Validation of request body
        if (bindingResult.hasErrors()) {
            logger.error("Fail to bind the request object with the Transaction class:");
            String errMessage = bindingResult.getAllErrors().toString();
            throw new WrongRequestFormatException(errMessage);
        }

        // Execute transaction in database
        transactionService.executeTransaction(transaction);

        // Display the account balance of from account
        return accountBalanceService.routeCheckBalance(transaction, request.getRequestURL().toString());
    }

    /**
     * Exception handler for request account ID not found
     *
     * @return HTTP response of status 404 Not Found
     */
    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onAccountNotFound(AccountNotFoundException e) {
        logger.error("Account not found in database! [account No:{}]", e.getAccountNo());
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorMessage(e.getMessage());
        resp.setErrorCode(HttpStatus.NOT_FOUND.value());
        return resp;
    }

    /**
     * Exception handler for wrong request format
     *
     * @return HTTP response of status 406 Not Acceptable
     */
    @ExceptionHandler({WrongRequestFormatException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse onWrongRequestFormat(WrongRequestFormatException e) {
        logger.error(e.getMessage());
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorMessage(e.getMessage());
        resp.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
        return resp;
    }

    /**
     * Exception handler for bad request
     *
     * @return HTTP response of status 400 Bad Request
     */
    @ExceptionHandler({
            IncorrectAccountInfoException.class,
            NotEnoughMoneyException.class,
            TransferAmountNotPositiveException.class,
            NotSupportedCurrencyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onBadRequest(RuntimeException e) {
        logger.error(e.getMessage());
        ErrorResponse resp = new ErrorResponse();
        resp.setErrorMessage(e.getMessage());
        resp.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return resp;
    }

}
