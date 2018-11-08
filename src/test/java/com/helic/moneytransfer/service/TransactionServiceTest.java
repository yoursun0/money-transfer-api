package com.helic.moneytransfer.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.helic.moneytransfer.db.builder.AccountBuilder;
import com.helic.moneytransfer.db.dao.TransactionDAO;
import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.exception.IncorrectAccountInfoException;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;
import com.helic.moneytransfer.exception.NotSupportedCurrencyException;
import com.helic.moneytransfer.exception.TransferAmountNotPositiveException;
import com.helic.moneytransfer.web.model.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test"})
public class TransactionServiceTest {

    private static final Account account1 = AccountBuilder.aBuilder
            .setId(521877048123L)
            .setName("CHAN TAI MAN")
            .setBalance(1000.00)
            .build();

    private static final Account account2 = AccountBuilder.aBuilder
            .setId(477333222431L)
            .setName("WONG SHEUNG")
            .setBalance(99999.00)
            .build();

    private static final Account account3 = AccountBuilder.aBuilder
            .setId(987654321000L)
            .setName("CHEUNG SIU MING")
            .setBalance(7777.00)
            .build();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionDAO transactionDAO;

    @InjectMocks
    private TransactionService service;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findById(account2.getId())).thenReturn(Optional.of(account2));
        Mockito.when(accountRepository.findById(account3.getId())).thenReturn(Optional.of(account3));
    }

    @Test
    public void successfulTransfer() {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(200.00);
        service.executeTransaction(transaction);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(account1.getId());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(account2.getId());
        Mockito.verify(transactionDAO, Mockito.times(1)).transferMoney(account1.getId(), account2.getId(), 200.00);
    }

    @Test(expected = NotSupportedCurrencyException.class)
    public void invalidCurrency() throws RuntimeException {
        try {
            Transaction transaction = new Transaction();
            transaction.setFromAccountNo(account1.getId());
            transaction.setToAccountNo(account2.getId());
            transaction.setToAccountName(account2.getName());
            transaction.setAmount(200.00);
            transaction.setCurrency("USD");
            service.executeTransaction(transaction);
        } catch (NotSupportedCurrencyException ex) {
            assertEquals("The currency [USD] is not supported by the money transfer system.", ex.getMessage());
            assertEquals("USD", ex.getCurrency());
            throw ex;
        }
        fail("Expected exception is not thrown.");
    }

    @Test(expected = TransferAmountNotPositiveException.class)
    public void zeroAmountTransfer() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(0.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = TransferAmountNotPositiveException.class)
    public void negativeAmountTransfer() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(-10.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = IncorrectAccountInfoException.class)
    public void toAccountSameAsFromAccount() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account1.getId());
        transaction.setToAccountName(account1.getName());
        transaction.setAmount(10.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = AccountNotFoundException.class)
    public void toAccountNumberNotExist() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(123456789012L);
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(10.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = AccountNotFoundException.class)
    public void fromAccountNumberNotExist() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(123456789012L);
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(10.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = IncorrectAccountInfoException.class)
    public void toAccountNameIncorrect() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account3.getName());
        transaction.setAmount(10.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void noEnoughMoney() throws RuntimeException {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(99999999.00);
        transaction.setCurrency("HKD");
        service.executeTransaction(transaction);
        fail("Expected exception is not thrown.");
    }
}