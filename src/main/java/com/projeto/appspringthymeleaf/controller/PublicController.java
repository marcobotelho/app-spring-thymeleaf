package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/senha-recuperar")
    public String getSenhaRecuperar() {
        return "senha-recuperar";
    }

    @PostMapping("/senha-recuperar")
    public String postSenhaRecuperar(@RequestParam(name = "email", required = true) String email, Model model)
            throws MessagingException {
        if (email == null || email.isEmpty()) {
            model.addAttribute("alertRecord", new AlertRecord("warning", "Atenção!",
                    "Email obrigatório."));
        } else {
            usuarioService.senhaRecuperar(email);
            model.addAttribute("alertRecord", new AlertRecord("success", "Sucesso!",
                    "Email com recuperação senha enviado para o email informado."));
        }

        return "senha-recuperar";
    }
}
