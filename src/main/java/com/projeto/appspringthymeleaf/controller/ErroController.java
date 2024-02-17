package com.projeto.appspringthymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.record.AlertRecord;

@Controller
@RequestMapping("/erro")
public class ErroController {

    private AlertRecord criarAlertaWarning(String mensagem) {
        return new AlertRecord("warning", "Atenção!", mensagem);
    }

    @GetMapping("")
    public String getError(@ModelAttribute("alertRecord") AlertRecord alertRecord) {
        return "erro";
    }

    @GetMapping("/403")
    public String getError403(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertRecord",
                criarAlertaWarning("Acesso negado!"));
        return "redirect:/erro";
    }

}
