package com.projeto.appspringthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.EstadoModel;

public interface EstadoRepository extends JpaRepository<EstadoModel, Long> {

}
