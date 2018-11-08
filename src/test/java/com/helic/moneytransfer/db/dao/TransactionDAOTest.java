package com.helic.moneytransfer.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.helic.moneytransfer.MoneyTransferApplication;
import com.helic.moneytransfer.db.builder.AccountBuilder;
import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.db.repo.TransactionRepository;
import com.helic.moneytransfer.exception.NotEnoughMoneyException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoneyTransferApplication.class)
@ActiveProfiles({"test"})
@ComponentScan(basePackages = {"com.helic.moneytransfer"})
@DirtiesContext
public class TransactionDAOTest {

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

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setupData() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        List<Account> initData = new ArrayList<>();
        initData.add(account1);
        initData.add(account2);
        initData.add(account3);
        accountRepository.saveAll(initData);
    }

    @After
    public void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void findAccountByIdTest() throws RuntimeException {
        long id1 = account1.getId();
        long id2 = account2.getId();
        long id3 = account3.getId();

        assertEquals(account1.getName(), accountRepository.findById(id1).get().getName());
        assertEquals(account2.getCurrency(), accountRepository.findById(id2).get().getCurrency());
        assertEquals(account3.getBalance(), accountRepository.findById(id3).get().getBalance(), EPISILON);
    }

    @Test
    public void transferMoney() throws RuntimeException {
        long id1 = account1.getId();
        long id2 = account2.getId();

        transactionDAO.transferMoney(id1, id2, 200.00);

        assertEquals(account1.getBalance() - 200, accountRepository.findById(id1).get().getBalance(), EPISILON);
        assertEquals(account2.getBalance() + 200, accountRepository.findById(id2).get().getBalance(), EPISILON);
        assertEquals(1, transactionRepository.count());

        com.helic.moneytransfer.db.entity.Transaction transactionData = transactionRepository.findAll().iterator().next();
        assertEquals(id1, transactionData.getFromAccountNo().longValue());
        assertEquals(id2, transactionData.getToAccountNo().longValue());
        assertEquals(200.00, transactionData.getAmount(), EPISILON);
    }

    @Test
    public void transferTooMuchMoney() {
        long id1 = account1.getId();
        long id2 = account2.getId();

        try {
            transactionDAO.transferMoney(id1, id2, 20000000.00);
        } catch (NotEnoughMoneyException ex) {
            assertEquals("Not enough money to transfer [20000000.00] from account. [accountNo:521877048123]", ex.getMessage());

            // Double confirm the transaction is not committed

            assertEquals(account1.getBalance(), accountRepository.findById(id1).get().getBalance(), EPISILON);
            assertEquals(account2.getBalance(), accountRepository.findById(id2).get().getBalance(), EPISILON);
            assertEquals(0, transactionRepository.count());
            return;
        }

        fail("Expected exception is not thrown.");
    }
}