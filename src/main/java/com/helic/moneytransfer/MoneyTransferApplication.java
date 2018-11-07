package com.helic.moneytransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MoneyTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.helic.moneytransfer.MoneyTransferApplication.class, args);
	}
}
