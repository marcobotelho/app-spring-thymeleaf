package com.projeto.appspringthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

}
