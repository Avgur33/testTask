package com.example.testTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.example.testTask")
public class TestTaskApplication {

	public static void main(String[] args) {

		SpringApplication.run(TestTaskApplication.class, args);
	}

}
