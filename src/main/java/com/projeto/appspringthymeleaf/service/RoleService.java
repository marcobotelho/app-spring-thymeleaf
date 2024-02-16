package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.RoleModel;
import com.projeto.appspringthymeleaf.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void save(RoleModel model) {
        if (model.getId() != null) {
            roleRepository.findById(model.getId())
                    .orElseThrow(() -> new RuntimeException("Role com id " + model.getId() + " não encontrado!"));
        }
        roleRepository.save(model);
    }

    public void delete(RoleModel model) {
        roleRepository.delete(model);
    }

    public void deleteById(Long id) {
        getById(id);
        roleRepository.deleteById(id);
    }

    public RoleModel getById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role com id " + id + " não encontrado!"));
    }

    public List<RoleModel> getAll(Long usuarioId) {
        return roleRepository.findAll();
    }
}
