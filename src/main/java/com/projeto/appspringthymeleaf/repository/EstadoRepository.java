package com.projeto.appspringthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.EstadoModel;

public interface EstadoRepository extends JpaRepository<EstadoModel, Long> {

    List<EstadoModel> findAllByOrderBySiglaAsc();

}
