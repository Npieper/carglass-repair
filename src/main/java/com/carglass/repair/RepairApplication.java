package com.carglass.repair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepairApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairApplication.class, args);
		System.out.println("Repair Application Started");
	}

}
