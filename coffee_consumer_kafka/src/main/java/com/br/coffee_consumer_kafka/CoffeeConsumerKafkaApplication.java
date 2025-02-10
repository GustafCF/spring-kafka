package com.br.coffee_consumer_kafka;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Coffe shop s",
				version = "1.0",
				description = "Coffee shop management system",
				contact = @Contact(
						name = "Gustavo",
						email = "pessoador@gmail.com",
						url = "https://www.linkedin.com/in/gustavo-c%C3%A9sar-franco-1375191b1/"
				)
		),
		servers = {
				@Server(url = "http://localhost:8080")
		}
)
@SpringBootApplication
public class CoffeeConsumerKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeConsumerKafkaApplication.class, args);
	}

}
