package com.helic.moneytransfer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@BootstrapWith(value = SpringBootTestContextBootstrapper.class)
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {MoneyTransferApplication.class})
public class MoneyTransferApplicationTest {

    @Test
    public void contextLoads() {
    }

}
