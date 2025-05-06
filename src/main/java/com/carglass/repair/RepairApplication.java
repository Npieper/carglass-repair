package com.carglass.repair;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Repair API",
				version = "v1",
				description = "API for managing repair orders"
		)
)
@SpringBootApplication
public class RepairApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairApplication.class, args);
		System.out.println("Repair Application Started");
	}

}
