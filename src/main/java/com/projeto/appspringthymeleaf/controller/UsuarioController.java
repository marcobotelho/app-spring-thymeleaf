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

import com.projeto.appspringthymeleaf.dto.UsuarioDTO;
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
	public String ini(@ModelAttribute("usuario") UsuarioDTO usuario,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
		model.addAttribute("usuario", usuario);
		model.addAttribute("listaPaginada", usuarioService.getListaPaginada(page));
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
	public String salvar(@Valid UsuarioDTO usuario, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws MessagingException {
		if (usuario.getId() == null) {
			usuarioService.save(usuario);
		} else {
			usuarioService.update(usuario.getId(), usuario);
		}
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Usuário salvo com sucesso!"));
		redirectAttributes.addFlashAttribute("usuario", new UsuarioDTO());
		return "redirect:/usuario";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		usuarioService.deleteById(id);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Usuário excluído com sucesso!"));
		return "redirect:/usuario";
	}

	@GetMapping("/viewTelefone/{id}")
	public String viewTelefone(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("usuarioId", id);
		return "redirect:/telefone";
	}

	@GetMapping("/{id}/perfis")
	public String usuarioPerfis(@PathVariable Long id, Model model) {
		model.addAttribute("usuario", usuarioService.getById(id));
		model.addAttribute("perfis", perfilService.getAll());
		model.addAttribute("selectedPerfilIds", usuarioService.getSelectedPerfilIds(id));
		return "usuario-perfis";
	}

	@PostMapping("/perfis")
	public String salvarUsuarioPerfis(@RequestParam(value = "usuarioId", required = true) Long usuarioId,
			@RequestParam(value = "selectedPerfilIds", required = false) Long[] selectedPerfilIds,
			RedirectAttributes redirectAttributes, Model model) {
		usuarioService.salvarUsuarioPerfis(usuarioId, selectedPerfilIds);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Usuário Perfis salvo com sucesso!"));
		return "redirect:/usuario/" + usuarioId + "/perfis";
	}

}
