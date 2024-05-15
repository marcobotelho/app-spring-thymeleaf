package com.projeto.appspringthymeleaf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projeto.appspringthymeleaf.model.TelefoneModel;

public interface TelefoneRepository extends JpaRepository<TelefoneModel, Long> {

	List<TelefoneModel> findByClienteId(Long clienteId);

	@Query("SELECT t FROM TelefoneModel t WHERE t.cliente.id = :clienteId")
	Page<TelefoneModel> findByClienteIdPage(@Param("clienteId") Long clienteId, Pageable pageable);
}
