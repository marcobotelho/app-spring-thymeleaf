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

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.TelefoneService;
import com.projeto.appspringthymeleaf.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/telefone")
public class TelefoneController {

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("")
	public String ini(@ModelAttribute("usuarioId") Long usuarioId, @ModelAttribute("telefone") TelefoneModel telefone,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		telefone.setUsuario(usuarioService.getById(usuarioId));
		model.addAttribute("telefone", telefone);
		model.addAttribute("telefones", telefoneService.getAll(usuarioId));
		model.addAttribute("tipos", TipoTelefoneEnum.values());

		return "telefone";
	}

<<<<<<< HEAD
	@GetMapping("/novo/{usuarioId}")
	public String novo(@PathVariable("usuarioId") Long usuarioId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("usuarioId", usuarioId);
		return "redirect:/telefone";
=======
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		TelefoneModel telefone = new TelefoneModel();
		try {
			telefone = telefoneService.getById(id);
			redirectAttributes.addFlashAttribute("telefone", telefone);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao editar telefone: " + e.getMessage()));
		}
		return "redirect:/telefone/usuarioId=" + telefone.getUsuario().getId();
>>>>>>> refs/remotes/origin/main
	}

	@PostMapping("")
	public String salvar(@Valid TelefoneModel telefone, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("telefone", telefone);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaErroValidacao(bindingResult));
		} else {
			try {
				telefoneService.save(telefone);
				redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Telefone salvo com sucesso!"));
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("telefone", telefone);
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro("Erro ao salvar telefone: " + e.getMessage()));
			}
		}
		redirectAttributes.addFlashAttribute("usuarioId", telefone.getUsuario().getId());
		return "redirect:/telefone";
	}

	@GetMapping("/editar/{usuarioId}&{id}")
	public String editar(@PathVariable("usuarioId") Long usuarioId, @PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("telefone", telefoneService.getById(id));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao editar telefone: " + e.getMessage()));
		}
		redirectAttributes.addFlashAttribute("usuarioId", usuarioId);
		return "redirect:/telefone";
	}

	@GetMapping("/excluir/{usuarioId}&{id}")
	public String delete(@PathVariable("usuarioId") Long usuarioId, @PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		try {
			telefoneService.deleteById(id);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Telefone excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir telefone: " + e.getMessage()));
		}
		redirectAttributes.addFlashAttribute("usuarioId", usuarioId);
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
}
