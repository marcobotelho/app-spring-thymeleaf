package com.projeto.appspringthymeleaf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.PerfilModel;

public interface PerfilRepository extends JpaRepository<PerfilModel, Long> {

    Optional<PerfilModel> findByNome(String nome);
}
