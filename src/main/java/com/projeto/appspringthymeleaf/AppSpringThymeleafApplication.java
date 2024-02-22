package com.projeto.appspringthymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:custom.properties")
public class AppSpringThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSpringThymeleafApplication.class, args);
	}

}
