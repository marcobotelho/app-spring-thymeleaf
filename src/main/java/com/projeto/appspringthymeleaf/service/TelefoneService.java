package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.TelefoneDTO;
import com.projeto.appspringthymeleaf.mapper.TelefoneMapper;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.repository.TelefoneRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TelefoneService {

	@Autowired
	private TelefoneRepository telefoneRepository;

	public void save(TelefoneDTO dto) {
		if (dto.getId() != null && !telefoneRepository.existsById(dto.getId())) {
			new RuntimeException("Telefone com id " + dto.getId() + " não encontrado!");
		}
		telefoneRepository.save(TelefoneMapper.converterParaModel(dto));
	}

	public void delete(TelefoneDTO dto) {
		telefoneRepository.delete(TelefoneMapper.converterParaModel(dto));
	}

	public void deleteById(Long id) {
		getById(id);
		telefoneRepository.deleteById(id);
	}

	public TelefoneDTO getById(Long id) {
		TelefoneModel model = telefoneRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Telefone com id " + id + " não encontrado!"));
		return TelefoneMapper.converterParaDTO(model);
	}

	public List<TelefoneDTO> getAll(Long clienteId) {
		return TelefoneMapper.converterParaDTOList(telefoneRepository.findByClienteId(clienteId));
	}
}
