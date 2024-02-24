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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.record.FormSenhaRecord;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/redefinir-senha")
public class RedefinirSenhaController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/{token}")
	public String ini(@PathVariable("token") String token,
			@ModelAttribute("formSenha") FormSenhaRecord formSenha,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		model.addAttribute("token", token);
		return "redefinir-senha";
	}

	// @GetMapping("/novo")
	// public String novo(FormSenhaRecord formSenha, RedirectAttributes
	// redirectAttributes, Model model) {
	// return "redirect:/redefinir-senha/" + formSenha.token();
	// }

	// @GetMapping("/editar/{id}")
	// public String editar(@PathVariable("id") Long id, RedirectAttributes
	// redirectAttributes) {
	// redirectAttributes.addFlashAttribute("usuario", usuarioService.getById(id));
	// return "redirect:/usuario";
	// }

	@PostMapping("")
	public String salvar(@Valid FormSenhaRecord formSenha, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErroValidacao(bindingResult));
		} else {
			try {
				usuarioService.redefinirSenha(formSenha);
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaSucesso("Senha redefinida com sucesso!"));
				return "redirect:/login?senhaRedefinida";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro(e.getMessage()));
			}
		}
		redirectAttributes.addFlashAttribute("formSenha", formSenha);
		return "redirect:/redefinir-senha/" + formSenha.token();
	}

	// @GetMapping("/excluir/{id}")
	// public String delete(@PathVariable Long id, RedirectAttributes
	// redirectAttributes) {
	// try {
	// usuarioService.deleteById(id);
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaSucesso("Usuário excluído com sucesso!"));
	// } catch (Exception e) {
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaErro("Erro ao excluir usuário: " + e.getMessage()));
	// }
	// return "redirect:/usuario";
	// }

	// @GetMapping("/viewTelefone/{id}")
	// public String viewTelefone(@PathVariable("id") Long id, Model model,
	// RedirectAttributes redirectAttributes) {
	// redirectAttributes.addFlashAttribute("usuarioId", id);
	// return "redirect:/telefone";
	// }

	private AlertRecord criarAlertaSucesso(String mensagem) {
		return new AlertRecord("success", "Sucesso!", mensagem);
	}

	private AlertRecord criarAlertaErro(String mensagem) {
		return new AlertRecord("danger", "Erro!", mensagem);
	}

	private AlertRecord criarAlertaErroValidacao(BindingResult bindingResult) {
		return new AlertRecord("warning", "Atenção!",
				montarMsgErroValidacao(bindingResult));
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

	// @GetMapping("/recuperar-senha/{id}")
	// public String recuperarSenha(@PathVariable("id") Long id, RedirectAttributes
	// redirectAttributes,
	// HttpServletRequest request) {
	// try {
	// String baseURL = request.getRequestURL().substring(0,
	// request.getRequestURL().indexOf(request.getServletPath()));
	// usuarioService.resetPassword(id, baseURL);
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaSucesso("Senha recuperada com sucesso! E-mail enviado com nova
	// senha."));
	// } catch (Exception e) {
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaErro("Erro ao recuperar senha: " + e.getMessage()));
	// }
	// return "redirect:/usuario";
	// }

	// @PostMapping("/redefinir-senha")
	// public String resetarSenha(@RequestParam("token") String token,
	// RedirectAttributes redirectAttributes,
	// HttpServletRequest request) {

	// try {
	// String usuarioEmail = jwtService.extrairUsuarioEmail(token);
	// UsuarioModel usuario = usuarioService.getByEmail(usuarioEmail);
	// String baseURL = request.getRequestURL().substring(0,
	// request.getRequestURL().indexOf(request.getServletPath()));
	// usuarioService.resetPassword(id, baseURL);
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaSucesso("Senha recuperada com sucesso! Acesse e-mail com nova
	// senha."));
	// } catch (Exception e) {
	// redirectAttributes.addFlashAttribute("alertRecord",
	// criarAlertaErro("Erro ao recuperar senha: " + e.getMessage()));
	// }
	// return "redefinir-senha";
	// }
}
