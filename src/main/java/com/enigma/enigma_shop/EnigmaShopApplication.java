package com.enigma.enigma_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnigmaShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnigmaShopApplication.class, args);
	}

}
