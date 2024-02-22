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
import com.projeto.appspringthymeleaf.service.ClienteService;
import com.projeto.appspringthymeleaf.service.TelefoneService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/telefone")
public class TelefoneController {

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private ClienteService clienteService;

	@GetMapping("")
	public String ini(@ModelAttribute("clienteId") Long clienteId, @ModelAttribute("telefone") TelefoneModel telefone,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		telefone.setCliente(clienteService.getById(clienteId));
		model.addAttribute("telefone", telefone);
		model.addAttribute("telefones", telefoneService.getAll(clienteId));
		model.addAttribute("tipos", TipoTelefoneEnum.values());

		return "telefone";
	}

	@GetMapping("/novo/{clienteId}")
	public String novo(@PathVariable("clienteId") Long clienteId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("clienteId", clienteId);
		return "redirect:/telefone";
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
		redirectAttributes.addFlashAttribute("clienteId", telefone.getCliente().getId());
		return "redirect:/telefone";
	}

	@GetMapping("/editar/{clienteId}&{id}")
	public String editar(@PathVariable("clienteId") Long clienteId, @PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("telefone", telefoneService.getById(id));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao editar telefone: " + e.getMessage()));
		}
		redirectAttributes.addFlashAttribute("clienteId", clienteId);
		return "redirect:/telefone";
	}

	@GetMapping("/excluir/{clienteId}&{id}")
	public String delete(@PathVariable("clienteId") Long clienteId, @PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		try {
			telefoneService.deleteById(id);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Telefone excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir telefone: " + e.getMessage()));
		}
		redirectAttributes.addFlashAttribute("clienteId", clienteId);
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
