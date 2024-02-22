package com.projeto.appspringthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.TelefoneModel;

public interface TelefoneRepository extends JpaRepository<TelefoneModel, Long> {

	List<TelefoneModel> findByClienteId(Long clienteId);
}
