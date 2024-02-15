package com.projeto.appspringthymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Retorna o nome da página Thymeleaf (login.html)
    }
}
