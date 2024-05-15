package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.dto.PerfilDTO;
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
	public String ini(@ModelAttribute("perfil") PerfilDTO perfil,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
		model.addAttribute("perfil", perfil);
		model.addAttribute("listaPaginada", perfilService.getListaPaginada(page));

		return "perfil";
	}

	@PostMapping("")
	public String salvar(@Valid PerfilDTO perfil, RedirectAttributes redirectAttributes) {
		perfilService.save(perfil);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Perfil salvo com sucesso!"));
		return "redirect:/perfil";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("perfil", perfilService.getById(id));
		return "redirect:/perfil";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		perfilService.deleteById(id);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Perfil excluído com sucesso!"));
		return "redirect:/perfil";
	}

	@GetMapping("/{id}/usuarios")
	public String perfilUsuarios(@PathVariable Long id, Model model) {

		model.addAttribute("perfil", perfilService.getById(id));
		model.addAttribute("usuarios", usuarioService.getAll());
		model.addAttribute("selectedUsuarioIds", perfilService.getSelectedUsuarioIds(id));

		return "perfil-usuarios";
	}

	@PostMapping("/usuarios")
	public String salvarPerfilUsuarios(@RequestParam(value = "perfilId", required = true) Long perfilId,
			@RequestParam(value = "selectedUsuarioIds", required = false) Long[] selectedUsuarioIds,
			RedirectAttributes redirectAttributes) {
		perfilService.salvarPerfilUsuarios(perfilId, selectedUsuarioIds);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Perfil Usuários salvo com sucesso!"));
		return "redirect:/perfil/" + perfilId + "/usuarios";
	}
}
