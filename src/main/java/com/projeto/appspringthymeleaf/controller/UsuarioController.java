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

import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.PerfilService;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PerfilService perfilService;

	@GetMapping("")
	public String ini(@ModelAttribute("usuario") UsuarioModel usuario,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		model.addAttribute("usuario", usuario);
		model.addAttribute("usuarios", usuarioService.getAll());
		return "usuario";
	}

	@GetMapping("/novo")
	public String novo(RedirectAttributes redirectAttributes) {
		return "redirect:/usuario";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("usuario", usuarioService.getById(id));
		return "redirect:/usuario";
	}

	@PostMapping("")
	public String salvar(@Valid UsuarioModel usuario, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaErroValidacao(bindingResult));
			redirectAttributes.addFlashAttribute("usuario", usuario);
		} else {
			try {
				if (usuario.getId() == null) {
					usuarioService.save(usuario);
				} else {
					usuarioService.update(usuario.getId(), usuario);
				}
				redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Usuário salvo com sucesso!"));
				redirectAttributes.addFlashAttribute("usuario", new UsuarioModel());
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro("Erro ao salvar usuário: " + e.getMessage()));
				redirectAttributes.addFlashAttribute("usuario", usuario);
			}
		}
		return "redirect:/usuario";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.deleteById(id);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Usuário excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir usuário: " + e.getMessage()));
		}
		return "redirect:/usuario";
	}

	@GetMapping("/viewTelefone/{id}")
	public String viewTelefone(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("usuarioId", id);
		return "redirect:/telefone";
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

	@GetMapping("/{id}/perfis")
	public String usuarioPerfis(@PathVariable Long id,
			RedirectAttributes redirectAttributes, Model model) {

		model.addAttribute("usuario", usuarioService.getById(id));
		model.addAttribute("perfis", perfilService.getAll());
		model.addAttribute("selectedPerfilIds", usuarioService.getSelectedPerfilIds(id));

		return "usuario-perfis";
	}

	@PostMapping("/perfis")
	public String salvarUsuarioPerfis(@RequestParam(value = "usuarioId", required = true) Long usuarioId,
			@RequestParam(value = "selectedPerfilIds", required = false) Long[] selectedPerfilIds,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			usuarioService.salvarUsuarioPerfis(usuarioId, selectedPerfilIds);
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaSucesso("Usuário Perfis salvo com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao salvar Usuário Perfis: " + e.getMessage()));
		}
		return "redirect:/usuario/" + usuarioId + "/perfis";
	}

	@GetMapping("/senha/recuperar/{email}")
	public String getSenhaRecuperar(@PathVariable("email") String email, Model model,
			RedirectAttributes redirectAttributes)
			throws MessagingException {
		if (email == null || email.isEmpty()) {
			redirectAttributes.addFlashAttribute("alertRecord", new AlertRecord("warning", "Atenção!",
					"Email obrigatório."));
		} else {
			usuarioService.senhaRecuperar(email);
			redirectAttributes.addFlashAttribute("alertRecord", new AlertRecord("success", "Sucesso!",
					"Email com recuperação senha enviado para o email do usuário."));
		}
		return "redirect:/usuario";
	}

	@GetMapping("/senha-alterar")
	public String getSenhaAlterar() {
		return "senha-alterar";
	}

	@PostMapping("/senha-alterar")
	public String postSenhaAlterar(@RequestParam(name = "senhaAtual") String senhaAtual,
			@RequestParam(name = "senhaNova") String senhaNova,
			@RequestParam(name = "senhaNovaConfirmacao") String senhaNovaConfirmacao,
			Model model) {

		try {
			usuarioService.senhaAlterar(senhaAtual, senhaNova, senhaNovaConfirmacao);
			model.addAttribute("alertRecord",
					new AlertRecord("success", "Sucesso!", "Senha alterada com sucesso."));
			return "senha-alterar";
		} catch (Exception e) {
			model.addAttribute("alertRecord",
					new AlertRecord("danger", "Erro!", e.getMessage()));
		}
		return "senha-alterar";
	}

}
