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

import com.projeto.appspringthymeleaf.model.ClienteModel;
import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.ClienteService;
import com.projeto.appspringthymeleaf.service.EstadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EstadoService estadoService;

	@GetMapping("")
	public String ini(@ModelAttribute("cliente") ClienteModel cliente,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		model.addAttribute("estados", estadoService.getAll());
		model.addAttribute("cliente", cliente);
		model.addAttribute("clientes", clienteService.getAll());
		return "cliente";
	}

	@GetMapping("/novo")
	public String novo(RedirectAttributes redirectAttributes) {
		return "redirect:/cliente";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ClienteModel cliente = clienteService.getById(id);
		redirectAttributes.addFlashAttribute("cliente", cliente);
		return "redirect:/cliente";
	}

	@PostMapping("")
	public String salvar(@Valid ClienteModel cliente, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaErroValidacao(bindingResult));
		} else {
			try {
				if (cliente.getId() == null) {
					clienteService.save(cliente);
				} else {
					clienteService.update(cliente.getId(), cliente);
				}
				redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Usuário salvo com sucesso!"));
				redirectAttributes.addFlashAttribute("cliente", new ClienteModel());
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("alertRecord",
						criarAlertaErro("Erro ao salvar usuário: " + e.getMessage()));
				redirectAttributes.addFlashAttribute("cliente", cliente);
			}
		}
		return "redirect:/cliente";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			clienteService.deleteById(id);
			redirectAttributes.addFlashAttribute("alertRecord", criarAlertaSucesso("Usuário excluído com sucesso!"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("alertRecord",
					criarAlertaErro("Erro ao excluir usuário: " + e.getMessage()));
		}
		return "redirect:/cliente";
	}

	@GetMapping("/viewTelefone/{id}")
	public String viewTelefone(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("clienteId", id);
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
