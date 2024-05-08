package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.ClienteModel;
import com.projeto.appspringthymeleaf.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public void save(ClienteModel model) {
		clienteRepository.save(model);
	}

	public void delete(ClienteModel model) {
		clienteRepository.delete(model);
	}

	public void update(Long id, ClienteModel model) {
		ClienteModel cliente = clienteRepository.getReferenceById(id);
		cliente.setNome(model.getNome());
		cliente.setEmail(model.getEmail());
		cliente.setIdade(model.getIdade());
		cliente.setDataNascimento(model.getDataNascimento());
		cliente.setCep(model.getCep());
		cliente.setEndereco(model.getEndereco());
		cliente.setBairro(model.getBairro());
		if (model.getMunicipio() != null && model.getMunicipio().getId() != null) {
			cliente.setMunicipio(model.getMunicipio());
		} else {
			cliente.setMunicipio(null);
		}
		clienteRepository.save(cliente);
	}

	public void deleteById(Long id) {
		getById(id);
		clienteRepository.deleteById(id);
	}

	public ClienteModel getById(Long id) {
		clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
		return clienteRepository.findById(id).get();
	}

	public List<ClienteModel> getAll() {
		return clienteRepository.findAll();
	}
}
