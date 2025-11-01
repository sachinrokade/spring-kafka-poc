package com.example.kafka.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.kafka.poc")
public class KafkaProducerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerAppApplication.class, args);
	}

}
