package com.helic.moneytransfer.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.exception.IncorrectAccountInfoException;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;
import com.helic.moneytransfer.exception.NotSupportedCurrencyException;
import com.helic.moneytransfer.exception.TransferAmountNotPositiveException;
import com.helic.moneytransfer.exception.WrongRequestFormatException;
import com.helic.moneytransfer.service.AccountBalanceService;
import com.helic.moneytransfer.service.TransactionService;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.ErrorResponse;
import com.helic.moneytransfer.web.model.Transaction;

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

    /**
     * GET Account Information API
     * Fetch the account information by account number
     *
     * @param accountNo account number to look up
     * @return an AccountBalance object containing key information of the account, including account holder name, currency, balance, etc.
     */
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

    /**
     * POST transaction API
     * Request for a money transfer. After transaction complete, return the account information of the from account, by calling the GET Account Information API above
     *
     * @param transaction   a JSON object containing key information for the money transfer, including from account no, to account no, currency, amount, etc.
     * @param bindingResult HTTP binding result
     * @param request       HTTP Servlet Request object
     * @return Returns an AccountBalance object containing key information of the from account after the money transfer.
     */
    @PostMapping(value = "/transaction")
    public @ResponseBody
    AccountBalance executeTransaction(
            @Valid @RequestBody Transaction transaction,
            BindingResult bindingResult,
            HttpServletRequest request
    ) throws WrongRequestFormatException, AccountNotFoundException {

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
        return accountBalanceService.routeCheckBalance(transaction.getFromAccountNo(), request.getRequestURL().toString());
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
