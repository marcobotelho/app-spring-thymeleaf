package com.projeto.appspringthymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.MunicipioModel;

public interface MunicipioRepository extends JpaRepository<MunicipioModel, Long> {

    List<MunicipioModel> findAllByOrderByNomeAsc();

    List<MunicipioModel> findByEstadoId(Long estadoId);
}
