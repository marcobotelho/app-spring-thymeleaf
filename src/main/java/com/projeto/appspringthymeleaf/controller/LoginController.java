package com.projeto.appspringthymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.appspringthymeleaf.record.AlertRecord;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "success", required = false) String success,
            @RequestParam(name = "error", required = false) String error,
            @ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
        if (success != null) {
            if (success.equals("logout")) {
                model.addAttribute("alertRecord",
                        new AlertRecord("success", "Sucesso!", "Logout realizado com sucesso."));
            } else if (success.equals("passwordChanged")) {
                model.addAttribute("alertRecord", new AlertRecord("success", "Sucesso!",
                        "Senha alterada com sucesso. Faça login com sua nova senha."));
            }
        } else if (error != null) {
            if (error.equals("unauthenticated")) {
                model.addAttribute("alertRecord",
                        new AlertRecord("danger", "Erro!", "Você precisa fazer login para acessar esta página."));
            } else if (error.equals("badCredentials")) {
                model.addAttribute("alertRecord",
                        new AlertRecord("danger", "Erro!", "Usúrio ou senha inválidos. Tente novamente."));
            }
        }
        return "login";
    }
}
