package com.projeto.appspringthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public void save(UsuarioModel model) {
		usuarioRepository.save(model);
	}

	public void delete(UsuarioModel model) {
		usuarioRepository.delete(model);
	}

	public void update(Long id, UsuarioModel model) {
		UsuarioModel user = usuarioRepository.getReferenceById(id);
		user.setNome(model.getNome());
		user.setEmail(model.getEmail());
		user.setIdade(model.getIdade());
		user.setDataNascimento(model.getDataNascimento());
		usuarioRepository.save(user);
	}

	public void deleteById(Long id) {
		usuarioRepository.deleteById(id);
	}

	public UsuarioModel getById(Long id) {
		return usuarioRepository.findById(id).get();
	}

	public Iterable<UsuarioModel> getAll() {
		return usuarioRepository.findAll();
	}
}
