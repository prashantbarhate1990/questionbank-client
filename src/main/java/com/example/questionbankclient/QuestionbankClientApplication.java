package com.example.questionbankclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QuestionbankClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionbankClientApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
	   return new RestTemplate();
	}
}
