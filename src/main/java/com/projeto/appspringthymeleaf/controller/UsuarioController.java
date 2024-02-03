package com.projeto.appspringthymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("")
	public ModelAndView getAll() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("usuario");
		modelAndView.addObject("usuario", new UsuarioModel());
		modelAndView.addObject("usuarios", usuarioService.getAll());
		return modelAndView;
	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/usuario");
		return modelAndView;
	}

	@GetMapping("/editar/{id}")
	public ModelAndView getById(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("usuario");
		modelAndView.addObject("usuario", usuarioService.getById(id));
		modelAndView.addObject("usuarios", usuarioService.getAll());
		return modelAndView;
	}

	@PostMapping("")
	public ModelAndView save(UsuarioModel usuario) {
		if (usuario.getId() == null) {
			usuarioService.save(usuario);
		} else {
			usuarioService.update(usuario.getId(), usuario);
		}
		ModelAndView modelAndView = new ModelAndView("redirect:/usuario");
		return modelAndView;
	}

	@GetMapping("/excluir/{id}")
	public ModelAndView delete(@PathVariable String id) {
		usuarioService.deleteById(Long.valueOf(id));
		ModelAndView modelAndView = new ModelAndView("redirect:/usuario");
		return modelAndView;
	}

}
