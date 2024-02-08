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

	@GetMapping("/usuarioId={usuarioId}")
	public String ini(@PathVariable("usuarioId") Long usuarioId, @ModelAttribute("telefone") TelefoneModel telefone,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		telefone.setUsuario(usuarioService.getById(usuarioId));
		model.addAttribute("telefone", telefone);
		model.addAttribute("telefones", telefoneService.getAll(usuarioId));
		model.addAttribute("tipos", TipoTelefoneEnum.values());

		return "telefone";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			TelefoneModel telefone = telefoneService.getById(id);
			redirectAttributes.addFlashAttribute("telefone", telefone);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao editar telefone: " + e.getMessage()));
		}
		return "redirect:/telefone/usuarioId=" + id;
	}

	@PostMapping("")
	public String salvar(@Valid TelefoneModel telefone, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("telefone", telefone);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaErroValidacao(bindingResult));
		} else {
			try {
				telefoneService.save(telefone);
				redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Telefone salvo com sucesso!"));
				redirectAttributes.addFlashAttribute("telefone", new TelefoneModel());
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro("Erro ao salvar telefone: " + e.getMessage()));
				redirectAttributes.addFlashAttribute("telefone", telefone);
			}
		}
		return "redirect:/telefone/usuarioId=" + telefone.getUsuario().getId();
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		TelefoneModel telefone = telefoneService.getById(id);
		try {
			telefoneService.deleteById(telefone.getId());
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Telefone excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir telefone: " + e.getMessage()));
		}
		return "redirect:/telefone/usuarioId=" + telefone.getUsuario().getId();
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
