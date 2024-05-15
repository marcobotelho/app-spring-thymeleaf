package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.dto.TelefoneDTO;
import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
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
	public String ini(@ModelAttribute("clienteId") Long clienteId, @ModelAttribute("telefone") TelefoneDTO telefone,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model) {
		telefone.setCliente(clienteService.getById(clienteId));
		model.addAttribute("telefone", telefone);
		model.addAttribute("telefones", telefoneService.getAll(clienteId));
		model.addAttribute("tipos", TipoTelefoneEnum.values());

		return "telefone";
	}

	@GetMapping("/novo/{clienteId}")
	public String novo(@PathVariable("clienteId") Long clienteId, RedirectAttributes redirectAttributes) {
		// redirectAttributes.addFlashAttribute("clienteId", clienteId);
		return "redirect:/telefone?clienteId=" + clienteId;
	}

	@PostMapping("")
	public String salvar(@Valid TelefoneDTO telefone, RedirectAttributes redirectAttributes, Model model) {
		telefoneService.save(telefone);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Telefone salvo com sucesso!"));
		// redirectAttributes.addFlashAttribute("clienteId",
		// telefone.getCliente().getId());
		return "redirect:/telefone?clienteId=" + telefone.getCliente().getId();
	}

	@GetMapping("/editar/{clienteId}&{id}")
	public String editar(@PathVariable("clienteId") Long clienteId, @PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("telefone", telefoneService.getById(id));
		// redirectAttributes.addFlashAttribute("clienteId", clienteId);
		return "redirect:/telefone?clienteId=" + clienteId;
	}

	@GetMapping("/excluir/{clienteId}&{id}")
	public String delete(@PathVariable("clienteId") Long clienteId, @PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		telefoneService.deleteById(id);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Telefone excluído com sucesso!"));
		// redirectAttributes.addFlashAttribute("clienteId", clienteId);
		return "redirect:/telefone?clienteId=" + clienteId;
	}
}
