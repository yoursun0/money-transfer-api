package com.helic.moneytransfer.service;

import java.util.Map;
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
import org.springframework.web.client.RestTemplate;

import com.helic.moneytransfer.db.builder.AccountBuilder;
import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.exception.AccountNotFoundException;
import com.helic.moneytransfer.web.model.AccountBalance;
import com.helic.moneytransfer.web.model.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test"})
public class AccountBalanceServiceTest {

    private static final Account account1 = AccountBuilder.aBuilder
            .setId(521877048123L)
            .setName("CHAN TAI MAN")
            .setBalance(1000.00)
            .setCurrency("HKD")
            .build();

    private static final Account account2 = AccountBuilder.aBuilder
            .setId(477333222431L)
            .setName("WONG SHEUNG")
            .setBalance(99999.00)
            .setCurrency("HKD")
            .build();

    private static final Account account3 = AccountBuilder.aBuilder
            .setId(987654321000L)
            .setName("CHEUNG SIU MING")
            .setBalance(7777.00)
            .setCurrency("HKD")
            .build();

    private static final double EPISILON = 0.000001;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private AccountBalanceService service;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findById(account2.getId())).thenReturn(Optional.of(account2));
        Mockito.when(accountRepository.findById(account3.getId())).thenReturn(Optional.of(account3));
    }

    @Test
    public void getAccountBalanceByAccountNo() {

        AccountBalance balance1 = service.getAccountBalanceByAccountNo(account1.getId());

        assertEquals(account1.getId(), balance1.getAccountNo());
        assertEquals(account1.getBalance(), balance1.getBalance(), EPISILON);
        assertEquals(account1.getCurrency(), balance1.getCurrency());
        assertEquals(account1.getName(), balance1.getAccountName());

        AccountBalance balance2 = service.getAccountBalanceByAccountNo(account2.getId());

        assertEquals(account2.getId(), balance2.getAccountNo());
        assertEquals(account2.getBalance(), balance2.getBalance(), EPISILON);
        assertEquals(account2.getCurrency(), balance2.getCurrency());
        assertEquals(account2.getName(), balance2.getAccountName());

        AccountBalance balance3 = service.getAccountBalanceByAccountNo(account3.getId());

        assertEquals(account3.getId(), balance3.getAccountNo());
        assertEquals(account3.getBalance(), balance3.getBalance(), EPISILON);
        assertEquals(account3.getCurrency(), balance3.getCurrency());
        assertEquals(account3.getName(), balance3.getAccountName());

    }

    @Test(expected = AccountNotFoundException.class)
    public void getAccountBalanceByAccountNoFailure() throws RuntimeException {
        AccountBalance balance = service.getAccountBalanceByAccountNo(1010101L);
        fail("Expected exception not thrown");
    }

    @Test
    public void testRouteCheckBalance() {

        String mockContextUrl = "mock-url/v1/money/";
        AccountBalance balance1 = service.mapToAccountBalance(account1);
        Mockito.when(restTemplate.getForObject(
                Mockito.eq(mockContextUrl + "account/{accountNo}"),
                Mockito.eq(AccountBalance.class),
                Mockito.any(Map.class)
        )).thenReturn(balance1);

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(0.00);
        transaction.setCurrency("HKD");
        AccountBalance balance = service.routeCheckBalance(transaction, mockContextUrl + "transaction");
        assertEquals(account1.getId(), balance.getAccountNo());
        assertEquals(account1.getName(), balance.getAccountName());
        assertEquals(account1.getBalance(), balance.getBalance(), EPISILON);
        assertEquals(account1.getCurrency(), balance.getCurrency());
    }
}