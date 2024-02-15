package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.repository.TelefoneRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TelefoneService {

	@Autowired
	private TelefoneRepository telefoneRepository;

	public void save(TelefoneModel model) {
		if (model.getId() != null) {
			telefoneRepository.findById(model.getId())
					.orElseThrow(() -> new RuntimeException("Telefone com id " + model.getId() + " não encontrado!"));
		}
		telefoneRepository.save(model);
	}

	public void delete(TelefoneModel model) {
		telefoneRepository.delete(model);
	}

	public void deleteById(Long id) {
		getById(id);
		telefoneRepository.deleteById(id);
	}

	public TelefoneModel getById(Long id) {
		return telefoneRepository.findById(id).orElseThrow( () -> new RuntimeException("Telefone com id " + id + " não encontrado!"));
	}

	public List<TelefoneModel> getAll(Long usuarioId) {
		return telefoneRepository.findByUsuarioId(usuarioId);
	}
}
