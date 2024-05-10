package com.projeto.appspringthymeleaf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    List<ClienteModel> findByNome(String nome);

    Optional<ClienteModel> findByEmail(String email);
}
