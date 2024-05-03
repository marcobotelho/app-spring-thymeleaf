package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.PerfilService;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

	@Autowired
	private PerfilService perfilService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("")
	public String ini(@ModelAttribute("perfil") PerfilModel perfil,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		model.addAttribute("perfil", perfil);
		model.addAttribute("perfis", perfilService.getAll());

		return "perfil";
	}

	@PostMapping("")
	public String salvar(@Valid PerfilModel perfil, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("perfil", perfil);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaErroValidacao(bindingResult));
		} else {
			try {
				perfilService.save(perfil);
				redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Perfil salvo com sucesso!"));
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("perfil", perfil);
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro("Erro ao salvar perfil: " + e.getMessage()));
			}
		}
		return "redirect:/perfil";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("perfil", perfilService.getById(id));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao editar perfil: " + e.getMessage()));
		}
		return "redirect:/perfil";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		try {
			perfilService.deleteById(id);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Perfil excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir perfil: " + e.getMessage()));
		}
		return "redirect:/perfil";
	}

	@GetMapping("/{id}/usuarios")
	public String perfilUsuarios(@PathVariable Long id,
			RedirectAttributes redirectAttributes, Model model) {

		model.addAttribute("perfil", perfilService.getById(id));
		model.addAttribute("usuarios", usuarioService.getAll());
		model.addAttribute("selectedUsuarioIds", perfilService.getSelectedUsuarioIds(id));

		return "perfil-usuarios";
	}

	@PostMapping("/usuarios")
	public String salvarPerfilUsuarios(@RequestParam(value = "perfilId", required = true) Long perfilId,
			@RequestParam(value = "selectedUsuarioIds", required = false) Long[] selectedUsuarioIds,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			perfilService.salvarPerfilUsuarios(perfilId, selectedUsuarioIds);
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaSucesso("Perfil Usuários salvo com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao salvar Perfil Usuários: " + e.getMessage()));
		}
		return "redirect:/perfil/" + perfilId + "/usuarios";
	}

	private AlertRecord criarAlertaSucesso(String mensagem) {
		return new AlertRecord("success", "Sucesso!", mensagem);
	}

	private AlertRecord criarAlertaErro(String mensagem) {
		return new AlertRecord("danger", "Erro!", mensagem);
	}

	private AlertRecord criarAlertaErroValidacao(BindingResult bindingResult) {
		return new AlertRecord("warning", "Atenção!", montarMsgErroValidacao(bindingResult));
	}

	private String montarMsgErroValidacao(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		bindingResult.getAllErrors().forEach(error -> {
			sb.append("<li>");
			sb.append(error.getDefaultMessage());
			sb.append("</li>");
		});
		sb.append("</ul>");
		return sb.toString();
	}

}
