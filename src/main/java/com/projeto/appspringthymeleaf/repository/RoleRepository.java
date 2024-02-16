package com.projeto.appspringthymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringthymeleaf.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

}
