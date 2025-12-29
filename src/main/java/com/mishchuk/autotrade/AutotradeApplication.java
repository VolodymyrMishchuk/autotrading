package com.mishchuk.autotrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutotradeApplication {
	public static void main(String[] args) {
		System.out.println("SPRING_KAFKA_BOOTSTRAP_SERVERS=" + System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));

		SpringApplication.run(AutotradeApplication.class, args);
	}
}
