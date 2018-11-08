package com.helic.moneytransfer.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helic.moneytransfer.MoneyTransferApplication;
import com.helic.moneytransfer.db.builder.AccountBuilder;
import com.helic.moneytransfer.db.dao.TransactionDAO;
import com.helic.moneytransfer.db.entity.Account;
import com.helic.moneytransfer.db.repo.AccountRepository;
import com.helic.moneytransfer.web.model.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@ComponentScan(basePackages = {"com.helic.moneytransfer"})
@DirtiesContext
public class WebControllerTest {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final double EPISILON = 0.000001;

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

    private WebController controller;

    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private AccountRepository accountRepository;

    @SpyBean
    MoneyTransferApplication application;

    @Before
    public void setUpMvc() throws Exception {
        controller = new WebController();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Before
    public void setupData() {
        accountRepository.deleteAll();
        List<Account> initData = new ArrayList<>();
        initData.add(account1);
        initData.add(account2);
        initData.add(account3);
        accountRepository.saveAll(initData);
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void getAccount() throws Exception {

        String url = "/v1/money/account/" + account1.getId();
        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json("{\"accountNo\":521877048123,\"accountName\":\"CHAN TAI MAN\",\"balance\":1000.0,\"currency\":\"HKD\"}"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void getAccountWihWrongAccountNo() throws Exception {

        String url = "/v1/money/account/123456789012";
        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().is(404))
                .andReturn();
    }

    @Test
    public void getAccountWihWrongAccountNumberFormat() throws Exception {

        String url = "/v1/money/account/123abc";
        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().is(406))
                .andReturn();
    }

    @Test
    public void postTransaction() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(200.00);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] requestJson = objectMapper.writeValueAsBytes(transaction);

        String url = "/v1/money/transaction";

        try {
            this.mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andReturn();
        } catch (NestedServletException ex) {
            Account fromAccount = accountRepository.findById(account1.getId()).get();
            Account toAccount = accountRepository.findById(account2.getId()).get();

            assertEquals(account1.getBalance() - 200, fromAccount.getBalance(), EPISILON);
            assertEquals(account2.getBalance() + 200, toAccount.getBalance(), EPISILON);
            return;
        }
        fail("NestedServletException is expected because the post transaction API should call get account balance API");
    }

    @Test
    public void postTransactionWithTooMuchMoney() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setFromAccountNo(account1.getId());
        transaction.setToAccountNo(account2.getId());
        transaction.setToAccountName(account2.getName());
        transaction.setAmount(999999999.00);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] requestJson = objectMapper.writeValueAsBytes(transaction);

        String url = "/v1/money/transaction";

        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().is(400))
                .andReturn();
    }
}