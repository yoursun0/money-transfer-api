package com.helic.moneytransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the money transfer application.
 * <p>
 * Launch and initialize the microservice via Spring boot
 */
@SpringBootApplication
@EnableAutoConfiguration
public class MoneyTransferApplication {

	private static final Logger log = LoggerFactory.getLogger(MoneyTransferApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(com.helic.moneytransfer.MoneyTransferApplication.class, args);
		log.info("********************* Launching Money Transfer Application *********************");
	}
}
