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
    public String loginPage(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            @RequestParam(required = false) String senhaRedefinida,
            @ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
        if (error != null) {
            model.addAttribute("alertRecord",
                    new AlertRecord("danger", "Erro!", "Credenciais inválidas. Tente novamente."));
        }
        if (logout != null) {
            model.addAttribute("alertRecord", new AlertRecord("success", "Sucesso!", "Logout realizado com sucesso."));
        }
        if (senhaRedefinida != null) {
            model.addAttribute("alertRecord", new AlertRecord("success", "Sucesso!",
                    "Senha redefinida com sucesso. Faça login com sua nova senha."));
        }
        return "login";
    }
}
