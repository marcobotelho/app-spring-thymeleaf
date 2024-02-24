package com.projeto.appspringthymeleaf;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.appspringthymeleaf.service.JwtService;

@SpringBootTest
class AppSpringThymeleafApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
	}

	@Test
	void gerarChaveSecretaJwt() {
		SecretKey chaveSecreta = jwtService.getChaveSecreta();
		String chaveString = jwtService.secretKeyToString(chaveSecreta);
		System.out.println("Chave Secreta: " + chaveString);
	}

}
