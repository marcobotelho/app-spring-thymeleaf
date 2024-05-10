package com.projeto.appspringthymeleaf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.appspringthymeleaf.model.MunicipioModel;

public interface MunicipioRepository extends JpaRepository<MunicipioModel, Long> {

    List<MunicipioModel> findAllByOrderByNomeAsc();

    List<MunicipioModel> findByEstadoId(Long estadoId);

    @Query("SELECT m FROM MunicipioModel m WHERE m.nome = ?1 AND m.estado.sigla = ?2")
    Optional<MunicipioModel> findByNomeAndEstadoSiglaIgnoreCase(String nome, String sigla);

    List<MunicipioModel> findByEstadoSigla(String sigla);
}
