package com.projeto.appspringthymeleaf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projeto.appspringthymeleaf.model.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    List<ClienteModel> findByNome(String nome);

    Optional<ClienteModel> findByEmail(String email);

    @Query(value = "SELECT * FROM cliente c " +
            " WHERE " +
            "(UPPER(c.nome) LIKE UPPER(CONCAT('%', :param_busca, '%')) " +
            " OR :param_busca IS NULL " +
            " OR :param_busca = '') " +
            " OR " +
            "(UPPER(c.email) LIKE UPPER(CONCAT('%', :param_busca, '%')) " +
            " OR :param_busca IS NULL " +
            " OR :param_busca = '')", nativeQuery = true)
    Page<ClienteModel> findByNomeEmailQuery(@Param("param_busca") String busca, Pageable pageable);
}
