package com.finops.financial_operations_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancialOperationsPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialOperationsPlatformApplication.class, args);
	}

}
