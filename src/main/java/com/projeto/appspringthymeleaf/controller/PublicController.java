package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/senha/recuperar")
    public String getSenhaRecuperar(@RequestParam(name = "email", required = false) String email,
            Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        } else {
            model.addAttribute("email", "");
        }
        return "senha-recuperar";
    }

    @PostMapping("/senha/recuperar")
    public String postSenhaRecuperar(@RequestParam(name = "email", required = true) String email, Model model)
            throws MessagingException {
        if (email == null || email.isEmpty()) {
            model.addAttribute("alertRecord", new AlertRecord("warning", "Atenção!",
                    "Email obrigatório."));
        } else {
            usuarioService.senhaRecuperar(email);
            model.addAttribute("alertRecord", new AlertRecord("success", "Sucesso!",
                    "Email com recuperação senha enviado para o email informado. Validade 10 minutos."));
        }

        return "senha-recuperar";
    }

    @GetMapping("/senha/alterar/{token}")
    public String getSenhaAlterar(@PathVariable("token") String token, Model model, HttpServletRequest request,
            HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("token", token);
        return "senha-alterar";
    }

    @PostMapping("/senha/alterar")
    public String postSenhaAlterar(@RequestParam(name = "token") String token,
            @RequestParam(name = "senhaNova") String senhaNova,
            @RequestParam(name = "senhaNovaConfirmacao") String senhaNovaConfirmacao,
            Model model, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.senhaAlterar(token, senhaNova, senhaNovaConfirmacao);
            redirectAttributes.addFlashAttribute("alertRecord",
                    new AlertRecord("success", "Sucesso!", "Senha alterada com sucesso. Efetue o login para acessar."));
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("alertRecord",
                    new AlertRecord("danger", "Erro!", e.getMessage()));
        }
        return "senha-alterar";
    }
}
