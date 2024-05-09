package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.ClienteDTO;
import com.projeto.appspringthymeleaf.mapper.ClienteMapper;
import com.projeto.appspringthymeleaf.mapper.MunicipioMapper;
import com.projeto.appspringthymeleaf.model.ClienteModel;
import com.projeto.appspringthymeleaf.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public void save(ClienteDTO dto) {
		if (dto.getId() != null && !clienteRepository.existsById(dto.getId())) {
			throw new RuntimeException("Cliente com id " + dto.getId() + " não encontrado!");
		}
		clienteRepository.save(ClienteMapper.converterParaModel(dto));
	}

	public void delete(ClienteDTO dto) {
		clienteRepository.delete(ClienteMapper.converterParaModel(dto));
	}

	public void update(Long id, ClienteDTO dto) {
		ClienteModel model = clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente com id " + id + " não encontrado!"));
		model.setNome(dto.getNome());
		model.setEmail(dto.getEmail());
		model.setIdade(dto.getIdade());
		model.setDataNascimento(dto.getDataNascimento());
		model.setCep(dto.getCep());
		model.setEndereco(dto.getEndereco());
		model.setBairro(dto.getBairro());
		if (dto.getMunicipio() != null && dto.getMunicipio().getId() != null) {
			model.setMunicipio(MunicipioMapper.converterParaModel(dto.getMunicipio()));
		} else {
			model.setMunicipio(null);
		}
		clienteRepository.save(model);
	}

	public void deleteById(Long id) {
		getById(id);
		clienteRepository.deleteById(id);
	}

	public ClienteDTO getById(Long id) {
		ClienteModel model = clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
		return ClienteMapper.converterParaDTO(model);
	}

	public List<ClienteDTO> getAll() {
		List<ClienteModel> models = clienteRepository.findAll();
		return ClienteMapper.converterParaDTOList(models);
	}
}
